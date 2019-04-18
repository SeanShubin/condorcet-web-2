package error

import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.dom.a
import react.dom.div
import react.dom.h1
import react.dom.p

fun RBuilder.error(navigateTo: (String) -> Unit, errorMessage: String) {
    div(classes = "single-column-flex") {
        h1 { +"Error" }
        p {
            errorMessage
        }
        a(href = "#") {
            +"Login"
            attrs.onClickFunction = {
                navigateTo("login")
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
