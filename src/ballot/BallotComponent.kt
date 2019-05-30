package ballot

import event.CondorcetEvent
import event.CondorcetEvent.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.Ballot
import model.Credentials
import org.w3c.dom.HTMLInputElement
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
                attrs {
                    value = ballot.electionName
                    readonly = true
                }
            }
            span {
                +"Voter"
            }
            input {
                attrs {
                    value = ballot.voterName
                    readonly = true
                }
            }
        }
        fieldSet(classes = "single-column-flex") {
            div(classes = "two-column-grid") {
                span {
                    +"rank"
                }
                span {
                    +"candidate"
                }
                for (ranking in ballot.rankings) {
                    span {
                        input {
                            attrs {
                                value = ranking.rank?.toString() ?: ""
                                size = "3"
                                onChangeFunction = { event ->
                                    val target = event.target as HTMLInputElement
                                    sendEvent(RankChanged(target.value, ranking.candidateName))
                                }
                            }
                        }
                    }
                    span {
                        +ranking.candidateName
                    }
                }

            }
            button {
                +"Cast Ballot"
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
            +"Election"
            attrs {
                onClickFunction = {
                    sendEvent(CondorcetEvent.LoadElectionRequest(credentials, ballot.electionName))
                }
            }
        }
        a(href = "#") {
            +"Elections"
            attrs {
                onClickFunction = {
                    sendEvent(CondorcetEvent.ListElectionsRequest(credentials))
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
