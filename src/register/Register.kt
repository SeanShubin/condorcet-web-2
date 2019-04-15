package register

import kotlinx.html.ButtonType
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

class Register : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
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
            button(type = ButtonType.button) { +"Register" }
            a(href = "#") {
                +"Login"
            }
        }
    }
}

fun RBuilder.register() = child(Register::class) {}
