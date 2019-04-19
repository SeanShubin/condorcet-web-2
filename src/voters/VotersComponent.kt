package voters

import kotlinx.html.ButtonType
import react.RBuilder
import react.dom.*

fun RBuilder.voters(electionName: String, voters: List<String>) {
    div(classes = "single-column-flex") {
        h1 { +"Voters" }
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
                        +"eligible voters"
                    }
                }
            }
            tbody {
                for (voter in voters) {
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
