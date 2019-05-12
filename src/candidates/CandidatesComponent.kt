package candidates

import event.Event
import kotlinx.html.ButtonType
import kotlinx.html.js.onClickFunction
import model.Credentials
import react.RBuilder
import react.dom.*

fun RBuilder.candidates(sendEvent: (Event) -> Unit,
                        credentials: Credentials,
                        electionName: String,
                        candidates: List<String>) {
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
            attrs {
                onClickFunction = {
                    sendEvent(Event.NavHomeRequest(credentials))
                }
            }
        }
        a(href = "#") {
            +"Logout"
            attrs {
                onClickFunction = {
                    sendEvent(Event.LogoutRequest)
                }
            }
        }
    }
}
