package register

import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

interface RegisterProps : RProps {
    var navigateTo: (String) -> Unit
}

class RegisterComponent : RComponent<RegisterProps, RState>() {
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
            button(type = ButtonType.button) {
                +"Register"
                attrs.onClickFunction = {
                    props.navigateTo("home")
                }
            }
            a(href = "#") {
                +"Login"
                attrs.onClickFunction = {
                    props.navigateTo("login")
                }
            }
        }
    }
}

fun RBuilder.register(navigateTo: (String) -> Unit) = child(RegisterComponent::class) {
    attrs.navigateTo = navigateTo
}
