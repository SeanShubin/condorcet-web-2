package ballot

import model.Ballot
import react.RBuilder
import react.dom.*

fun RBuilder.ballot(ballot: Ballot) {
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
