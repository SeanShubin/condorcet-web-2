package election

import event.CondorcetEvent
import event.CondorcetEvent.*
import kotlinx.html.InputType
import kotlinx.html.js.onClickFunction
import model.Credentials
import model.Election
import model.StringConversions.dateToString
import react.RBuilder
import react.dom.*

fun RBuilder.election(sendEvent: (CondorcetEvent) -> Unit,
                      credentials: Credentials,
                      election: Election) {
    div(classes = "single-column-flex") {
        h1 { +"Election" }
        div(classes = "two-column-grid") {
            +"Election"
            input {
                attrs["value"] = election.name
            }
            +"Owner"
            input {
                attrs["value"] = election.ownerName
            }
            +"Status"
            input {
                attrs["value"] = election.status.description
            }
            +"Start"
            input {
                attrs["value"] = dateToString(election.start)
                attrs["placeholder"] = "YYYY-MM-DD HH:MM"
            }
            +"End"
            input {
                attrs["value"] = dateToString(election.end)
                attrs["placeholder"] = "YYYY-MM-DD HH:MM"
            }
        }
        span {
            input(type = InputType.checkBox) {}
            +"Secret Ballot"
        }
        a(href = "#") {
            +"Candidates (${election.candidateCount})"
        }
        a(href = "#") {
            +"Voters (${election.voterCount})"
        }
        button {
            +"Done Editing"
        }
        button {
            +"Start Now"
        }
        button {
            +"End Now"
        }
        a(href = "#") {
            +"Elections"
            attrs {
                onClickFunction = {
                    sendEvent(ListElectionsRequest(credentials))
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
