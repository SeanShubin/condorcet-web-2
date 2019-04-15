package candidates

import kotlinx.html.ButtonType
import model.ElectionAndCandidates
import react.RBuilder
import react.RComponent
import react.RState
import react.dom.*

class CandidatesComponent : RComponent<CandidatesProps, RState>() {
    override fun RBuilder.render() {
        div(classes = "single-column-flex") {
            h1 { +"Candidates" }
            div(classes = "two-column-grid") {
                span {
                    +"Election"
                }
                input {
                    attrs["value"] = props.electionAndCandidates.electionName
                }
            }
            table {
                thead {
                    tr {
                        th {
                            +"candidate"
                        }
                    }
                }
                tbody {
                    for (candidate in props.electionAndCandidates.candidates) {
                        tr {
                            td {
                                +candidate
                            }
                        }
                    }
                }
            }
            a(href = "#") {
                +"View candidates as text"
            }
            textArea {}
            button(type = ButtonType.button) { +"Update Candidates" }
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

fun RBuilder.candidates(electionAndCandidates: ElectionAndCandidates) = child(CandidatesComponent::class) {
    attrs.electionAndCandidates = electionAndCandidates
}
