package home

import event.Event
import kotlinx.html.js.onClickFunction
import model.Credentials
import react.RBuilder
import react.dom.a
import react.dom.div
import react.dom.h1

fun RBuilder.home(sendEvent: (Event) -> Unit, credentials: Credentials) {
    div(classes = "single-column-flex") {
        h1 { +"Home" }
        a(href = "#") {
            +"Elections"
            attrs.onClickFunction = {
                sendEvent(Event.NavElectionsRequest)
            }
        }
        a(href = "#") {
            +"Ballots"
            attrs.onClickFunction = {
                sendEvent(Event.NavBallotsRequest(credentials))
            }
        }
        a(href = "#") {
            +"Logout"
            attrs.onClickFunction = {
                sendEvent(Event.LogoutRequest)
            }
        }
    }
}
