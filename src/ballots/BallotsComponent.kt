package ballots

import event.CondorcetEvent
import kotlinx.html.js.onClickFunction
import model.Ballot
import model.Credentials
import react.RBuilder
import react.dom.*

fun RBuilder.ballots(sendEvent: (CondorcetEvent) -> Unit,
                     credentials: Credentials,
                     voterName: String,
                     ballots: List<Ballot>) {
    div(classes = "single-column-flex") {
        h1 {
            +"Ballots"
        }
        div(classes = "two-column-grid") {
            span {
                +"Voter"
            }
            input {
                attrs {
                    value = voterName
                    readonly = true
                }
            }
        }
        if (ballots.isEmpty()) {
            span {
                +"No ballots for $voterName"
            }
        } else {
            table {
                thead {
                    tr {
                        th {
                            +"ballot"
                        }
                        th {
                            +"election"
                        }
                    }
                }
                tbody {
                    ballots.forEach {
                        tr {
                            td {
                                a(href = "#") {
                                    +it.action.displayName
                                }
                            }
                            td {
                                +it.electionName
                            }
                        }
                    }
                }
            }
        }
        a(href = "#") {
            +"Home"
            attrs {
                onClickFunction = {
                    sendEvent(CondorcetEvent.NavHomeRequest(credentials))
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
