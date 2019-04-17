package home

import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.dom.a
import react.dom.div
import react.dom.h1

fun RBuilder.home(navigateTo: (String) -> Unit) {
    div(classes = "single-column-flex") {
        h1 { +"Home" }
        a(href = "#") {
            +"Elections"
        }
        a(href = "#") {
            +"Ballots"
        }
        a(href = "#") {
            +"Logout"
            attrs.onClickFunction = {
                navigateTo("login")
            }
        }
    }
}
