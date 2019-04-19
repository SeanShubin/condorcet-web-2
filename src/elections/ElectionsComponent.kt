package elections

import model.Election
import react.RBuilder
import react.dom.*

fun RBuilder.elections(elections: List<Election>) {
    div(classes = "single-column-flex") {
        h1 { +"Elections" }
        div {
            input {
                attrs["placeholder"] = "election name"
            }
            button {
                +"Create"
            }
        }
        div {
            input {
                attrs["placeholder"] = "election name"
            }
            select {
                for (election in elections) {
                    option {
                        +election.name
                    }
                }
            }
            button {
                +"Copy"
            }
        }
        table {
            thead {
                tr {
                    th {
                        +"edit"
                    }
                    th {
                        +"owner"
                    }
                    th {
                        +"name"
                    }
                    th {
                        +"start"
                    }
                    th {
                        +"end"
                    }
                    th {
                        +"secret ballot"
                    }
                }
            }
            tbody {
                for (election in elections) {
                    tr {
                        td {
                            a(href = "#") {
                                +"edit"
                            }
                        }
                        td {
                            +election.ownerName
                        }
                        td {
                            +election.name
                        }
                        td {
                            +election.startString
                        }
                        td {
                            +election.endString
                        }
                        td {
                            +election.secretBallotString
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
