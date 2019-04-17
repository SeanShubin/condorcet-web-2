package register

import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.dom.*

fun RBuilder.register(navigateTo: (String) -> Unit) {
    div(classes = "single-column-flex") {
        h1 { +"Register" }
        input {
            attrs["placeholder"] = "name"
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
            attrs.onClickFunction = {
                navigateTo("home")
            }
        }
        a(href = "#") {
            +"Login"
            attrs.onClickFunction = {
                navigateTo("login")
            }
        }
    }
}
