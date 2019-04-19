package login

import event.Event
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*

interface LoginProps : RProps {
    var sendEvent: (Event) -> Unit
    var errorMessage: String?
}

interface LoginState : RState {
    var nameOrEmail: String
    var password: String
}

class LoginComponent : RComponent<LoginProps, LoginState>() {
    override fun LoginState.init() {
        nameOrEmail = ""
        password = ""
    }

    override fun RBuilder.render() {
        div(classes = "single-column-flex") {
            h1 { +"Login" }
            val errorMessage = props.errorMessage
            if (errorMessage != null) {
                p(classes = "error") {
                    +errorMessage
                }
            }
            input {
                attrs {
                    placeholder = "name or email"
                    value = state.nameOrEmail
                    onChangeFunction = { event ->
                        val target = event.target as HTMLInputElement
                        setState {
                            nameOrEmail = target.value
                        }
                    }
                }
            }
            input {
                attrs {
                    placeholder = "password"
                    type = InputType.password
                    value = state.password
                    onChangeFunction = { event ->
                        val target = event.target as HTMLInputElement
                        setState {
                            password = target.value
                        }
                    }
                }

            }
            button(type = ButtonType.button) {
                +"Login"
                attrs.onClickFunction = {
                    props.sendEvent(Event.LoginRequest(state.nameOrEmail, state.password))
                }
            }
            a(href = "#") {
                +"Register"
                attrs.onClickFunction = {
                    props.sendEvent(Event.NavRegisterRequest)
                }
            }
            a(href = "#") {
                +"Prototype"
                attrs.onClickFunction = {
                    props.sendEvent(Event.NavPrototypeRequest)
                }
            }
        }
    }
}

fun RBuilder.login(sendEvent: (Event) -> Unit, errorMessage: String?) = child(LoginComponent::class) {
    attrs.sendEvent = sendEvent
    attrs.errorMessage = errorMessage
}
