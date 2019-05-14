package election

import event.CondorcetEvent
import event.CondorcetEvent.*
import kotlinx.html.InputType
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.Credentials
import model.Election
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*

interface ElectionProps : RProps {
    var sendEvent: (CondorcetEvent) -> Unit
    var errorMessage: String?
    var election: Election
    var credentials: Credentials
}

interface ElectionState : RState {
    var startDate: String
    var endDate: String
}

class ElectionComponent : RComponent<ElectionProps, ElectionState>() {
    override fun ElectionState.init(props: ElectionProps) {
        startDate = props.election.startString
        endDate = props.election.endString
    }

    override fun RBuilder.render() {
        val sendEvent = props.sendEvent
        val election = props.election
        val credentials = props.credentials
        val errorMessage = props.errorMessage
        div(classes = "single-column-flex") {
            h1 { +"Election" }
            if (errorMessage != null) {
                p(classes = "error") {
                    +errorMessage
                }
            }
            div(classes = "two-column-grid") {
                +"Election"
                input(classes = "readonly") {
                    attrs {
                        value = election.name
                        readonly = true
                    }
                }
                +"Owner"
                input(classes = "readonly") {
                    attrs {
                        value = election.ownerName
                        readonly = true
                    }
                }
                +"Status"
                input(classes = "readonly") {
                    attrs {
                        value = election.status.description
                        readonly = true
                    }
                }
                +"Start"
                input {
                    attrs {
                        placeholder = "YYYY-MM-DD HH:MM"
                        value = state.startDate
                        onChangeFunction = { event ->
                            val target = event.target as HTMLInputElement
                            setState {
                                startDate = target.value
                            }
                        }
                        onBlurFunction = { event ->
                            sendEvent(UpdateStartDate(credentials, election.name, state.startDate))
                        }
                    }
                }
                +"End"
                input {
                    attrs {
                        placeholder = "YYYY-MM-DD HH:MM"
                        value = state.endDate
                        onChangeFunction = { event ->
                            val target = event.target as HTMLInputElement
                            setState {
                                endDate = target.value
                            }
                        }
                        onBlurFunction = { event ->
                            sendEvent(UpdateEndDate(credentials, election.name, state.endDate))
                        }
                    }
                }
            }
            span {
                input(type = InputType.checkBox) {
                    attrs {
                        checked = election.secretBallot
                        onChangeFunction = { event ->
                            val target = event.target as HTMLInputElement
                            sendEvent(UpdateSecretBallot(credentials, election.name, target.checked))
                        }
                    }
                }
                +"Secret Ballot"
            }
            a(href = "#") {
                +"Candidates (${election.candidateCount})"
                attrs {
                    onClickFunction = {
                        sendEvent(ListCandidatesRequest(credentials, election.name))
                    }
                }

            }
            a(href = "#") {
                +"Voters (${election.voterCount})"
                attrs {
                    onClickFunction = {
                        sendEvent(ListVotersRequest(credentials, election.name))
                    }
                }
            }
            button {
                +"Done Editing"
                attrs {
                    onClickFunction = {
                        sendEvent(DoneEditingRequest(credentials, election.name))
                    }
                }
            }
            button {
                +"Start Now"
                attrs {
                    onClickFunction = {
                        sendEvent(StartNowRequest(credentials, election.name))
                    }
                }
            }
            button {
                +"End Now"
                attrs {
                    onClickFunction = {
                        sendEvent(EndNowRequest(credentials, election.name))
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

fun RBuilder.election(sendEvent: (CondorcetEvent) -> Unit,
                      credentials: Credentials,
                      election: Election,
                      errorMessage: String?) = child(ElectionComponent::class) {
    attrs.sendEvent = sendEvent
    attrs.credentials = credentials
    attrs.election = election
    attrs.errorMessage = errorMessage
}
