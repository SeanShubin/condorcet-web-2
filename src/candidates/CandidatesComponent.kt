package candidates

import event.CondorcetEvent
import event.CondorcetEvent.LogoutRequest
import event.CondorcetEvent.NavHomeRequest
import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import model.Credentials
import react.RBuilder
import react.dom.*

fun RBuilder.candidates(sendEvent: (CondorcetEvent) -> Unit,
                        credentials: Credentials,
                        electionName: String,
                        candidates: List<String>) {
    console.log("candidates", candidates)
    div(classes = "single-column-flex") {
        h1 { +"Candidates" }
        div(classes = "two-column-grid") {
            span {
                +"Election"
            }
            input {
                attrs {
                    value = electionName
                    readonly = true
                }
            }
        }
        if (candidates.isEmpty()) {
            span { +"No Candidates" }
        } else {
            table {
                thead {
                    tr {
                        th {
                            +"candidates"
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
        }
        a(href = "#") {
            +"View candidates as text"
        }
        textArea {
            attrs {
                placeholder = "Add one candidate per line here"
            }
        }
        button(type = ButtonType.button) { +"Update Candidates" }
        a(href = "#") {
            +"Election"
        }
        a(href = "#") {
            +"Home"
            attrs {
                onClickFunction = {
                    sendEvent(NavHomeRequest(credentials))
                }
            }
        }
        a(href = "#") {
            +"Logout"
            attrs {
                onClickFunction = {
                    sendEvent(LogoutRequest)
                }
            }
        }
    }
}
