package login

import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.dom.*

fun RBuilder.login(navigateTo: (String) -> Unit) {
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
                navigateTo("home")
            }
        }
        a(href = "#") {
            +"Register"
            attrs.onClickFunction = {
                navigateTo("register")
            }
        }
        a(href = "#") {
            +"Prototype"
            attrs.onClickFunction = {
                navigateTo("prototype")
            }
        }
    }
}
