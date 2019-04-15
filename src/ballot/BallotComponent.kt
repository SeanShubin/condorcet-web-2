package ballot

import model.Ballot
import react.RBuilder
import react.RComponent
import react.RState
import react.dom.*

class BallotComponent : RComponent<BallotProps, RState>() {
    override fun RBuilder.render() {
        div(classes = "single-column-flex") {
            h1 {
                +"Ballot"
            }
            div(classes = "two-column-grid") {
                span {
                    +"Election"
                }
                input {
                    attrs["value"] = props.ballot.electionName
                }
                span {
                    +"Voter"
                }
                input {
                    attrs["value"] = props.ballot.voterName
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
                    for (ranking in props.ballot.rankings) {
                        tr {
                            td {
                                input {
                                    attrs["value"] = ranking.rank
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
            }
            a(href = "#") {
                +"Logout"
            }
        }
    }
}

fun RBuilder.ballot(ballot: Ballot) = child(BallotComponent::class) {
    attrs.ballot = ballot
}
