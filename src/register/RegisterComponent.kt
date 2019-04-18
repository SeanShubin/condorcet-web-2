package register

import event.Event
import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.ReactElement
import react.dom.*

fun RBuilder.register(sendEvent: (Event) -> Unit) {
    div(classes = "single-column-flex") {
        h1 { +"Register" }
        val nameField: ReactElement = input {
            attrs["placeholder"] = "name"
        }
        val emailField = input {
            attrs["placeholder"] = "email"
        }
        val passwordField = input {
            attrs["placeholder"] = "password"
            attrs["type"] = "password"
        }
        val confirmPasswordField = input {
            attrs["placeholder"] = "confirm password"
            attrs["type"] = "password"
        }
        button(type = ButtonType.button) {
            +"Register"
            attrs.onClickFunction = {
                sendEvent(Event.RegisterRequest(nameField ))
            }
        }
        a(href = "#") {
            +"Login"
            attrs.onClickFunction = {
                sendEvent(Event.NavLoginRequest)
            }
        }
    }
}
