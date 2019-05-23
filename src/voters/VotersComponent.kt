package voters

import event.CondorcetEvent
import event.CondorcetEvent.LogoutRequest
import event.CondorcetEvent.NavHomeRequest
import kotlinx.html.ButtonType
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.Credentials
import model.StringConversions.toLines
import org.w3c.dom.HTMLTextAreaElement
import react.*
import react.dom.*
import kotlin.math.max

interface VotersProps : RProps {
    var sendEvent: (CondorcetEvent) -> Unit
    var credentials: Credentials
    var electionName: String
    var voters: List<String>
    var isAllVoters: Boolean
}

interface VotersState : RState {
    var votersString: String
    var pendingEdits: Boolean
    var allVoters: Boolean
}


class VotersComponent : RComponent<VotersProps, VotersState>() {
    override fun componentWillReceiveProps(nextProps: VotersProps) {
        setState {
            votersString = nextProps.voters.joinToString("\n")
            pendingEdits = false
            allVoters = nextProps.isAllVoters
        }
    }

    override fun componentDidMount() {
        setState {
            votersString = props.voters.joinToString("\n")
            pendingEdits = false
            allVoters = props.isAllVoters
        }
    }

    override fun RBuilder.render() {
        val electionName = props.electionName
        val voters = props.voters
        val sendEvent = props.sendEvent
        val credentials = props.credentials
        div(classes = "single-column-flex") {
            h1 { +"Voters" }
            div(classes = "two-column-grid") {
                span {
                    +"Election:"
                }
                span {
                    +electionName
                }
            }
            fieldSet(classes = "single-column-flex") {
                legend { +"Eligible Voters" }
                textArea {
                    attrs {
                        placeholder = "Add one voter per line here"
                        value = state.votersString
                        rows = voters.size.toString()
                        cols = max(20, voters.map { it.length }.max() ?: 1).toString()
                        onChangeFunction = { event ->
                            val target = event.target as HTMLTextAreaElement
                            setState {
                                pendingEdits = true
                                allVoters = false
                                votersString = target.value
                            }
                        }

                    }
                }
                button(type = ButtonType.button) {
                    +"Update Voters"
                    attrs {
                        onClickFunction = {
                            sendEvent(CondorcetEvent.UpdateVotersRequest(
                                    credentials,
                                    electionName,
                                    state.votersString.toLines()))
                            setState {
                                pendingEdits = false
                                allVoters = false
                            }
                        }
                        disabled = !state.pendingEdits
                    }
                }
                button(type = ButtonType.button) {
                    +"Set to All Voters"
                    attrs {
                        onClickFunction = {
                            sendEvent(CondorcetEvent.UpdateToAllVotersRequest(
                                    credentials,
                                    electionName))
                            setState {
                                pendingEdits = false
                                allVoters = true
                            }
                        }
                        disabled = state.allVoters
                    }
                }
            }
            a(href = "#") {
                +"Election"
                attrs {
                    onClickFunction = {
                        sendEvent(CondorcetEvent.LoadElectionRequest(credentials, electionName))
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

fun RBuilder.voters(sendEvent: (CondorcetEvent) -> Unit,
                    credentials: Credentials,
                    electionName: String,
                    voters: List<String>,
                    isAllVoters: Boolean) = child(VotersComponent::class) {
    attrs.sendEvent = sendEvent
    attrs.credentials = credentials
    attrs.electionName = electionName
    attrs.voters = voters
    attrs.isAllVoters = isAllVoters
}
