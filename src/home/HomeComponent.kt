package home

import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.a
import react.dom.div
import react.dom.h1

class HomeComponent : RComponent<RProps, RState>() {
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
            }
        }
    }
}

fun RBuilder.home() = child(HomeComponent::class) {}
