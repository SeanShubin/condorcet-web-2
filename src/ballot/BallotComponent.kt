package ballot

import event.CondorcetEvent
import event.CondorcetEvent.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.Credentials
import org.w3c.dom.HTMLInputElement
import pages.BallotPage
import react.RBuilder
import react.dom.*

fun RBuilder.ballot(sendEvent: (CondorcetEvent) -> Unit, credentials: Credentials, ballot: BallotPage) {
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

                for (rankingWithIndex in ballot.rankings.withIndex()) {
                    val (index, ranking) = rankingWithIndex
                    span {
                        input {
                            attrs {
                                value = ranking.rank
                                size = "3"
                                onChangeFunction = { event ->
                                    val target = event.target as HTMLInputElement
                                    sendEvent(RankChanged(index, target.value))
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
                    sendEvent(ListBallotsRequest(credentials))
                }
            }
        }
        a(href = "#") {
            +"Election"
            attrs {
                onClickFunction = {
                    sendEvent(LoadElectionRequest(credentials, ballot.electionName))
                }
            }
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
