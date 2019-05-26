package ballots

import event.CondorcetEvent
import event.CondorcetEvent.LogoutRequest
import event.CondorcetEvent.NavHomeRequest
import kotlinx.html.js.onClickFunction
import model.Ballot
import model.Credentials
import model.StringConversions
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
                            +"status"
                        }
                        th {
                            +"election"
                        }
                        th {
                            +"when cast"
                        }
                    }
                }
                tbody {
                    ballots.forEach { ballot ->
                        tr {
                            td {
                                +if (ballot.isActive) "active" else "completed"
                            }
                            td {
                                a(href = "#") {
                                    +ballot.electionName
                                }
                            }
                            td {
                                if (ballot.whenCast == null) {
                                    +"not yet"
                                } else {
                                    +StringConversions.dateToString(ballot.whenCast)
                                }
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
