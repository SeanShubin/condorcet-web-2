package voters

import kotlinx.html.ButtonType
import model.ElectionAndVoters
import react.RBuilder
import react.RComponent
import react.RState
import react.dom.*

class VotersComponent : RComponent<VotersProps, RState>() {
    override fun RBuilder.render() {
        div(classes = "single-column-flex") {
            h1 { +"Voters" }
            div(classes = "two-column-grid") {
                span {
                    +"Election"
                }
                input {
                    attrs["value"] = props.electionAndVoters.electionName
                }
            }
            table {
                thead {
                    tr {
                        th {
                            +"eligible voters"
                        }
                    }
                }
                tbody {
                    for (voter in props.electionAndVoters.voters) {
                        tr {
                            td {
                                +voter
                            }
                        }
                    }
                }
            }
            a(href = "#") {
                +"View voters as text"
            }
            textArea {}
            button(type = ButtonType.button) { +"Update Voters" }
            button(type = ButtonType.button) { +"Add All Voters" }
            a(href = "#") {
                +"Election"
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

fun RBuilder.voters(electionAndVoters: ElectionAndVoters) = child(VotersComponent::class) {
    attrs.electionAndVoters = electionAndVoters
}
