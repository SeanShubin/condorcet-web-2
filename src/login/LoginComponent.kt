package login

import event.Event
import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.dom.*

fun RBuilder.login(sendEvent: (Event) -> Unit) {
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
                sendEvent(Event.NavHomeRequest)
            }
        }
        a(href = "#") {
            +"Register"
            attrs.onClickFunction = {
                sendEvent(Event.NavRegisterRequest)
            }
        }
        a(href = "#") {
            +"Prototype"
            attrs.onClickFunction = {
                sendEvent(Event.NavPrototypeRequest)
            }
        }
    }
}
