package login

import kotlinx.html.ButtonType
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

class Login : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        div(classes = "single-column-flex") {
            h1 { +"Login" }
            input {
                attrs["placeholder"] = "name or email"
                attrs["type"] = "password"
            }
            button(type = ButtonType.button) { +"Login" }
            a(href = "#") {
                +"Register"
            }
        }
    }
}

fun RBuilder.login() = child(Login::class) {}
