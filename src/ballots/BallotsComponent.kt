package ballots

import model.VoterAndBallots
import react.RBuilder
import react.RComponent
import react.RState
import react.dom.*

class BallotsComponent : RComponent<BallotsProps, RState>() {
    override fun RBuilder.render() {
        val (voter, ballots) = props.voterAndBallots
        div(classes = "single-column-flex") {
            h1 {
                +"Ballots"
            }
            div(classes = "two-column-grid") {
                span {
                    +"Voter"
                }
                input {
                    attrs["value"] = voter
                }
            }
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
            a(href = "#") {
                +"Home"
            }
            a(href = "#") {
                +"Logout"
            }
        }
    }
}

fun RBuilder.ballots(voterAndBallots: VoterAndBallots) = child(BallotsComponent::class) {
    attrs.voterAndBallots = voterAndBallots
}
