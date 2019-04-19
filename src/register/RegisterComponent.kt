package register

import event.Event
import kotlinx.html.ButtonType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import react.*
import react.dom.*

interface RegisterState : RState {
    var name: String
    var email: String
    var password: String
    var confirmPassword: String
}

interface RegisterProps : RProps {
    var sendEvent: (Event) -> Unit
}

class RegisterComponent : RComponent<RegisterProps, RegisterState>() {
    override fun RBuilder.render() {
        div(classes = "single-column-flex") {
            h1 { +"Register" }
            input {
                attrs["placeholder"] = "name"
                attrs.onChangeFunction = { event ->
                    setState {
                    }
                }
            }
            input {
                attrs["placeholder"] = "email"
            }
            input {
                attrs["placeholder"] = "password"
                attrs["type"] = "password"
            }
            input {
                attrs["placeholder"] = "confirm password"
                attrs["type"] = "password"
            }
            button(type = ButtonType.button) {
                +"Register"
            }
            a(href = "#") {
                +"Login"
                attrs.onClickFunction = {
                    props.sendEvent(Event.NavLoginRequest)
                }
            }
        }
    }
}

fun RBuilder.register(sendEvent: (Event) -> Unit) = child(RegisterComponent::class) {
    attrs.sendEvent = sendEvent
}
