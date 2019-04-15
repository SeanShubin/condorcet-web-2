package ballots

import react.RBuilder
import react.RComponent
import react.RState
import react.dom.*

class BallotsComponent : RComponent<BallotsProps, RState>() {
    override fun RBuilder.render() {
        div(classes = "single-column-flex") {
            h1 {
                +"Ballots"
            }
            div(classes = "two-column-grid") {
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
                            +"ballot"
                        }
                        th {
                            +"election"
                        }
                    }
                }
                tbody {
                    props.ballots.forEach {
                        tr {
                            td {
                                button {
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

fun RBuilder.ballots(props: BallotsProps) = child(BallotsComponent::class) {
    attrs.voterName = props.voterName
    attrs.ballots = props.ballots
}
