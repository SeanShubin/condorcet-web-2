package candidates

import conversion.StringConversions.toLines
import event.CondorcetEvent
import event.CondorcetEvent.*
import kotlinx.html.ButtonType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.Credentials
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.*
import kotlin.math.max

interface CandidatesProps : RProps {
    var sendEvent: (CondorcetEvent) -> Unit
    var credentials: Credentials
    var electionName: String
    var candidates: List<String>
}

interface CandidatesState : RState {
    var candidatesString: String
    var pendingEdits: Boolean
}

class CandidatesComponent : RComponent<CandidatesProps, CandidatesState>() {
    override fun componentDidMount() {
        setState {
            candidatesString = props.candidates.joinToString("\n")
            pendingEdits = false
        }
    }

    override fun RBuilder.render() {
        val sendEvent = props.sendEvent
        val credentials = props.credentials
        val electionName = props.electionName
        val candidates = props.candidates
        div(classes = "single-column-flex") {
            h1 { +"Candidates" }
            div(classes = "two-column-grid") {
                span {
                    +"Election:"
                }
                span {
                    +electionName
                }
            }
            fieldSet(classes = "single-column-flex") {
                legend { +"Candidates" }
                textArea {
                    attrs {
                        placeholder = "Add one candidate per line here"
                        value = state.candidatesString
                        rows = candidates.size.toString()
                        cols = max(20, candidates.map { it.length }.max() ?: 1).toString()
                        onChangeFunction = { event ->
                            val target = event.target as HTMLTextAreaElement
                            setState {
                                pendingEdits = true
                                candidatesString = target.value
                            }
                        }
                    }
                }
                button(type = ButtonType.button) {
                    +"Update Candidates"
                    attrs {
                        onClickFunction = {
                            sendEvent(UpdateCandidatesRequest(
                                    credentials,
                                    electionName,
                                    state.candidatesString.toLines()))
                            setState {
                                pendingEdits = false
                            }
                        }
                        disabled = !state.pendingEdits
                    }
                }
            }
            a(href = "#") {
                +"Election"
                attrs {
                    onClickFunction = {
                        sendEvent(LoadElectionRequest(credentials, electionName))
                    }
                }
            }
            a(href = "#") {
                +"Elections"
                attrs {
                    onClickFunction = {
                        sendEvent(ListElectionsRequest(credentials))
                    }
                }
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
}

fun RBuilder.candidates(sendEvent: (CondorcetEvent) -> Unit,
                        credentials: Credentials,
                        electionName: String,
                        candidates: List<String>) = child(CandidatesComponent::class) {
    attrs.sendEvent = sendEvent
    attrs.credentials = credentials
    attrs.electionName = electionName
    attrs.candidates = candidates
}
