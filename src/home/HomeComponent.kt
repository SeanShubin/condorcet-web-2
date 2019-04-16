package home

import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.a
import react.dom.div
import react.dom.h1

interface HomeProps : RProps {
    var navigateTo: (String) -> Unit
}

class HomeComponent : RComponent<HomeProps, RState>() {
    override fun RBuilder.render() {
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
                    props.navigateTo("login")
                }
            }
        }
    }
}

fun RBuilder.home(navigateTo: (String) -> Unit) = child(HomeComponent::class) {
    attrs.navigateTo = navigateTo
}
