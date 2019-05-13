package home

import event.CondorcetEvent
import kotlinx.html.js.onClickFunction
import model.Credentials
import react.RBuilder
import react.dom.a
import react.dom.div
import react.dom.h1

fun RBuilder.home(sendEvent: (CondorcetEvent) -> Unit, credentials: Credentials) {
    div(classes = "single-column-flex") {
        h1 { +"Home" }
        a(href = "#") {
            +"Elections"
            attrs {
                onClickFunction = {
                    sendEvent(CondorcetEvent.ListElectionsRequest(credentials))
                }
            }
        }
        a(href = "#") {
            +"Ballots"
            attrs {
                onClickFunction = {
                    sendEvent(CondorcetEvent.ListBallotsRequest(credentials))
                }
            }
        }
        a(href = "#") {
            +"Logout"
            attrs {
                onClickFunction = {
                    sendEvent(CondorcetEvent.LogoutRequest)
                }
            }
        }
    }
}
