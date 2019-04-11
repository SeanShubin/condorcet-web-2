package ballot

import react.RBuilder
import react.RComponent
import react.RState
import react.dom.*

class Ballot : RComponent<BallotProps, RState>() {
    override fun RBuilder.render() {
        div(classes = "single-column-flex"){
            h1 {
                +"Ballot"
            }
            div(classes = "two-column-grid") {
                span {
                    +"Election"
                }
                input {
                    attrs["value"] = "Election A"
                }
                span {
                    +"Voter"
                }
                input {
                    attrs["value"] = "Voter A"
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
                    for (ranking in props.rankings) {
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
            a(href="#") {
                +"Home"
            }
            a(href="#") {
                +"Logout"
            }
        }
    }
}

fun RBuilder.ballot(props:BallotProps) = child(Ballot::class) {
    attrs.electionName = props.electionName
    attrs.voterName = props.voterName
    attrs.rankings = props.rankings
}
