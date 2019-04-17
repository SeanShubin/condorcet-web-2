package candidates

import kotlinx.html.ButtonType
import model.ElectionAndCandidates
import react.RBuilder
import react.dom.*

fun RBuilder.candidates(electionAndCandidates: ElectionAndCandidates) {
    val (electionName, candidates) = electionAndCandidates
    div(classes = "single-column-flex") {
        h1 { +"Candidates" }
        div(classes = "two-column-grid") {
            span {
                +"Election"
            }
            input {
                attrs["value"] = electionName
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
                for (candidate in candidates) {
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
