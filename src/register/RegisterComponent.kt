package register

import dynamic.EventUtil
import event.CondorcetEvent
import event.CondorcetEvent.NavLoginRequest
import event.CondorcetEvent.RegisterRequest
import kotlinx.html.ButtonType
import kotlinx.html.InputType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*

interface RegisterState : RState {
    var name: String
    var email: String
    var password: String
    var confirmPassword: String
}

interface RegisterProps : RProps {
    var sendEvent: (CondorcetEvent) -> Unit
    var errorMessage: String?
}

class RegisterComponent : RComponent<RegisterProps, RegisterState>() {
    override fun RegisterState.init() {
        name = ""
        email = ""
        password = ""
        confirmPassword = ""
    }
    override fun RBuilder.render() {
        div(classes = "single-column-flex") {
            attrs {
                onKeyUpFunction = { target: org.w3c.dom.events.Event ->
                    if (EventUtil.key(target) === "Enter") {
                        registerButtonPressed(target)
                    }
                }
            }
            h1 { +"Register" }
            val errorMessage = props.errorMessage
            if (errorMessage != null) {
                p(classes = "error") {
                    +errorMessage
                }
            }
            input {
                attrs {
                    placeholder = "name"
                    onChangeFunction = { event ->
                        val target = event.target as HTMLInputElement
                        setState {
                            name = target.value
                        }
                    }
                }
            }
            input {
                attrs {
                    placeholder = "email"
                    onChangeFunction = { event ->
                        val target = event.target as HTMLInputElement
                        setState {
                            email = target.value
                        }
                    }
                }
            }
            input {
                attrs {
                    placeholder = "password"
                    type = InputType.password
                    onChangeFunction = { event ->
                        val target = event.target as HTMLInputElement
                        setState {
                            password = target.value
                        }
                    }
                }
            }
            input {
                attrs {
                    placeholder = "confirm password"
                    type = InputType.password
                    onChangeFunction = { event ->
                        val target = event.target as HTMLInputElement
                        setState {
                            confirmPassword = target.value
                        }
                    }
                }
            }
            button(type = ButtonType.button) {
                +"Register"
                attrs {
                    onClickFunction = ::registerButtonPressed
                }
            }
            a(href = "#") {
                +"Login"
                attrs {
                    onClickFunction = {
                        props.sendEvent(NavLoginRequest)
                    }
                }
            }
        }
    }

    private fun registerButtonPressed(event: org.w3c.dom.events.Event) {
        props.sendEvent(RegisterRequest(
                state.name,
                state.email,
                state.password,
                state.confirmPassword))
    }
}

fun RBuilder.register(sendEvent: (CondorcetEvent) -> Unit, errorMessage: String?) = child(RegisterComponent::class) {
    attrs.sendEvent = sendEvent
    attrs.errorMessage = errorMessage
}
