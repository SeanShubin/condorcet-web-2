package ballot

import conversion.FrontEndToApi.toApi
import conversion.StringConversions.toStringSecond
import event.CondorcetEvent
import event.CondorcetEvent.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import pages.BallotPage
import react.RBuilder
import react.dom.*

fun RBuilder.ballot(sendEvent: (CondorcetEvent) -> Unit, ballot: BallotPage) {
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
            span {
                +"When Cast"
            }
            input {
                attrs {
                    value = ballot.whenCast.toStringSecond()
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
                attrs {
                    onClickFunction = {
                        val apiBallot = ballot.toApi()
                        sendEvent(CastBallotRequest(
                                ballot.credentials, ballot.electionName, ballot.voterName, apiBallot.rankings))
                    }
                    disabled = !ballot.isEdited
                }
            }
        }
        a(href = "#") {
            +"Ballots"
            attrs {
                onClickFunction = {
                    sendEvent(ListBallotsRequest(ballot.credentials))
                }
            }
        }
        a(href = "#") {
            +"Election"
            attrs {
                onClickFunction = {
                    sendEvent(LoadElectionRequest(ballot.credentials, ballot.electionName))
                }
            }
        }
        a(href = "#") {
            +"Elections"
            attrs {
                onClickFunction = {
                    sendEvent(ListElectionsRequest(ballot.credentials))
                }
            }
        }
        a(href = "#") {
            +"Home"
            attrs {
                onClickFunction = {
                    sendEvent(NavHomeRequest(ballot.credentials))
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
