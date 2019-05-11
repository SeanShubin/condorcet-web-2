package effect

import app.Environment
import event.Event
import model.Credentials

interface Effect {
    fun apply(handleEvent: (Event) -> Unit, environment: Environment)
    data class Dispatch(val event: Event) : Effect {
        override fun apply(handleEvent: (Event) -> Unit, environment: Environment) {
            handleEvent(event)
        }

    }

    data class Login(val nameOrEmail: String, val password: String) : Effect {
        override fun apply(handleEvent: (Event) -> Unit, environment: Environment) {
            environment.api.login(nameOrEmail, password).then { credentials ->
                handleEvent(Event.NavHomeRequest(credentials))
            }.catch { throwable ->
                handleEvent(Event.LoginFailure(throwable.message ?: "Unable to login"))
            }
        }
    }
    data class Register(val name: String,
                        val email: String,
                        val password: String,
                        val confirmPassword: String) : Effect {
        override fun apply(handleEvent: (Event) -> Unit, environment: Environment) {
            if (password != confirmPassword) {
                handleEvent(Event.RegisterFailure("password does not match confirmation"))
            } else {
                environment.api.register(name, password, password).then { credentials ->
                    handleEvent(Event.NavHomeRequest(credentials))
                }.catch { throwable ->
                    handleEvent(Event.RegisterFailure(throwable.message ?: "Unable to register"))
                }
            }
        }
    }

    data class ListElections(val credentials: Credentials) : Effect {
        override fun apply(handleEvent: (Event) -> Unit, environment: Environment) {
            environment.api.listElections(credentials).then { elections ->
                handleEvent(Event.ListElectionsSuccess(elections))
            }.catch { throwable ->
                handleEvent(Event.ListElectionsFailure(throwable.message ?: "Unable to list elections"))
            }
        }
    }
    data class SetPathName(val pathName: String) : Effect {
        override fun apply(handleEvent: (Event) -> Unit, environment: Environment) {
            environment.setPathName(pathName)
        }
    }
}
