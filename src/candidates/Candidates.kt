package candidates

import kotlinx.html.ButtonType
import react.RBuilder
import react.RComponent
import react.RState
import react.dom.*

class Candidates : RComponent<CandidatesProps, RState>() {
    override fun RBuilder.render() {
        div(classes = "single-column-flex") {
            h1 { +"Candidates" }
            div(classes = "two-column-grid") {
                span {
                    +"Election"
                }
                input {
                    attrs["value"] = "Election A"
                }
            }
            table {
                thead {
                    tr {
                        th {
                            +"candidate"
                        }
                        th {
                            +"remove"
                        }
                    }
                }
                tbody {
                    for (candidate in props.candidates) {
                        tr {
                            td {
                                +candidate
                            }
                            td {
                                button {
                                    +"remove"
                                }
                            }
                        }
                    }
                }
            }
            textArea {}
            button(type = ButtonType.button) { +"Add Candidates" }
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

fun RBuilder.candidates(candidates: List<String>) = child(Candidates::class) {
    attrs.candidates = candidates
}
