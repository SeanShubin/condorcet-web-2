package login

import event.Event
import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

interface LoginProps : RProps {
    var sendEvent: (Event) -> Unit
}

class LoginComponent : RComponent<LoginProps, RState>() {
    override fun RBuilder.render() {
        div(classes = "single-column-flex") {
            h1 { +"Login" }
            input {
                attrs["placeholder"] = "name or email"
            }
            input {
                attrs["placeholder"] = "password"
                attrs["type"] = "password"
            }
            button(type = ButtonType.button) {
                +"Login"
                attrs.onClickFunction = {
                    props.sendEvent(Event.NavHomeRequest)
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

fun RBuilder.login(sendEvent: (Event) -> Unit) = child(LoginComponent::class) {
    attrs.sendEvent = sendEvent
}
