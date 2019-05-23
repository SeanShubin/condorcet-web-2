package ballot

import event.CondorcetEvent
import event.CondorcetEvent.LogoutRequest
import event.CondorcetEvent.NavHomeRequest
import kotlinx.html.js.onClickFunction
import model.Ballot
import model.Credentials
import react.RBuilder
import react.dom.*

fun RBuilder.ballot(sendEvent: (CondorcetEvent) -> Unit, credentials: Credentials, ballot: Ballot) {
    div(classes = "single-column-flex") {
        h1 {
            +"Ballot"
        }
        div(classes = "two-column-grid") {
            span {
                +"Election"
            }
            input {
                attrs["value"] = ballot.electionName
            }
            span {
                +"Voter"
            }
            input {
                attrs["value"] = ballot.voterName
            }
        }
        table {
            thead {
                tr {
                    th {
                        +"rank"
                    }
                    th {

                        +"candidate"
                    }
                }
            }
            tbody {
                for (ranking in ballot.rankings) {
                    tr {
                        td {
                            input {
                                attrs["value"] = ranking.rank ?: ""
                                attrs["size"] = 3
                            }
                        }
                        td {
                            +ranking.candidateName
                        }
                    }
                }
            }
        }
        button {
            +"Cast Ballot"
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
