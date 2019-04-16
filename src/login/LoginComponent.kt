package login

import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

interface LoginProps : RProps {
    var navigateTo: (String) -> Unit
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
                    props.navigateTo("home")
                }
            }
            a(href = "#") {
                +"Register"
                attrs.onClickFunction = {
                    props.navigateTo("register")
                }
            }
            a(href = "#") {
                +"Prototype"
                attrs.onClickFunction = {
                    props.navigateTo("prototype")
                }
            }
        }
    }
}

fun RBuilder.login(navigateTo: (String) -> Unit) = child(LoginComponent::class) {
    attrs.navigateTo = navigateTo
}
