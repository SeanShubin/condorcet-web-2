package ballots

import model.Ballot
import react.RBuilder
import react.dom.*

fun RBuilder.ballots(voterName: String, ballots: List<Ballot>) {
    div(classes = "single-column-flex") {
        h1 {
            +"Ballots"
        }
        div(classes = "two-column-grid") {
            span {
                +"Voter"
            }
            input {
                attrs["value"] = voterName
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
