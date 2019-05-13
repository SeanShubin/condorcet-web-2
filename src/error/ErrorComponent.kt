package error

import event.CondorcetEvent
import event.CondorcetEvent.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.dom.a
import react.dom.div
import react.dom.h1
import react.dom.p

fun RBuilder.errorPage(sendEvent: (CondorcetEvent) -> Unit, errorMessage: String) {
    div(classes = "single-column-flex") {
        h1 { +"Error" }
        p(classes = "error") {
            +errorMessage
        }
        a(href = "#") {
            +"Login"
            attrs.onClickFunction = {
                sendEvent(NavLoginRequest)
            }
        }
        a(href = "#") {
            +"Register"
            attrs.onClickFunction = {
                sendEvent(NavRegisterRequest)
            }
        }
        a(href = "#") {
            +"Prototype"
            attrs.onClickFunction = {
                sendEvent(NavPrototypeRequest)
            }
        }
    }
}
