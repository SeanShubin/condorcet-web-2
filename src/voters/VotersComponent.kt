package voters

import event.CondorcetEvent
import event.CondorcetEvent.LogoutRequest
import event.CondorcetEvent.NavHomeRequest
import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import model.Credentials
import react.RBuilder
import react.dom.*
import kotlin.math.max

fun RBuilder.voters(sendEvent: (CondorcetEvent) -> Unit,
                    credentials: Credentials,
                    electionName: String,
                    voters: List<String>) {
    div(classes = "single-column-flex") {
        h1 { +"Voters" }
        div(classes = "two-column-grid") {
            span {
                +"Election"
            }
            input {
                attrs {
                    value = electionName
                    readonly = true
                }
            }
        }
        fieldSet(classes = "single-column-flex") {
            legend { +"Eligible Voters" }
            textArea {
                attrs {
                    placeholder = "Add one candidate per line here"
                    rows = voters.size.toString()
                    cols = max(20, voters.map { it.length }.max() ?: 1).toString()
                }
            }
            button(type = ButtonType.button) { +"Update Voters" }
            button(type = ButtonType.button) { +"Add All Voters" }
        }
        a(href = "#") {
            +"Election"
            attrs {
                onClickFunction = {
                    sendEvent(CondorcetEvent.LoadElectionRequest(credentials, electionName))
                }
            }
        }
        a(href = "#") {
            +"Home"
            attrs {
                onClickFunction = {
                    sendEvent(NavHomeRequest(credentials))
                }
            }
        }
        a(href = "#") {
            +"Logout"
            attrs {
                onClickFunction = {
                    sendEvent(LogoutRequest)
                }
            }
        }
    }
}
