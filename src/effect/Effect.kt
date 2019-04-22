package effect

import app.Environment
import event.Event

interface Effect {
    fun apply(handleEvent: (Event) -> Unit, environment: Environment)
    data class Dispatch(val event: Event) : Effect {
        override fun apply(handleEvent: (Event) -> Unit, environment: Environment) {
            handleEvent(event)
        }

    }

    data class Login(val nameOrEmail: String, val password: String) : Effect {
        override fun apply(handleEvent: (Event) -> Unit, environment: Environment) {
            environment.api.login(nameOrEmail, password).then {
                handleEvent(Event.NavHomeRequest)
            }.catch { throwable ->
                handleEvent(Event.LoginFailure(throwable.message ?: "<no message>"))
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
                environment.api.register(name, password, password).then {
                    handleEvent(Event.NavHomeRequest)
                }.catch { throwable ->
                    handleEvent(Event.RegisterFailure(throwable.message ?: "<no message>"))
                }
            }
        }
    }
}
