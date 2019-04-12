package elections

import react.RBuilder
import react.RComponent
import react.RState
import react.dom.*

class Elections : RComponent<ElectionsProps, RState>() {
    override fun RBuilder.render() {
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
                    for (election in props.elections) {
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
                    for (election in props.elections) {
                        tr {
                            td {
                                button {
                                    +"edit"
                                }
                            }
                            td {
                                +election.owner
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
}

fun RBuilder.elections(props: ElectionsProps) = child(Elections::class) {
    attrs.elections = props.elections
}
