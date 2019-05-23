package election

import event.CondorcetEvent
import event.CondorcetEvent.*
import kotlinx.html.InputType
import kotlinx.html.js.onBlurFunction
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.Election
import model.Election.ElectionStatus.*
import org.w3c.dom.HTMLInputElement
import pages.ElectionPage
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.*

interface ElectionProps : RProps {
    var sendEvent: (CondorcetEvent) -> Unit
    var page: ElectionPage
}

class ElectionComponent : RComponent<ElectionProps, RState>() {
    override fun RBuilder.render() {
        val sendEvent = props.sendEvent
        val credentials = props.page.credentials
        val electionName = props.page.electionName
        val ownerName = props.page.ownerName
        val status = props.page.status
        val start = props.page.start
        val end = props.page.end
        val secretBallot = props.page.secretBallot
        val candidateCount = props.page.candidateCount
        val voterCount = props.page.voterCount
        val errorMessage = props.page.errorMessage
        div(classes = "single-column-flex") {
            h1 { +"Election" }
            if (errorMessage != null) {
                p(classes = "error") {
                    +errorMessage
                }
            }
            div(classes = "two-column-grid") {
                +"Election:"
                span {
                    +electionName
                }
                +"Owner:"
                span {
                    +ownerName
                }
                +"Status:"
                span {
                    +status.description
                }
                +"Start:"
                input {
                    attrs {
                        placeholder = "YYYY-MM-DD HH:MM"
                        value = start
                        onChangeFunction = { event ->
                            val target = event.target as HTMLInputElement
                            sendEvent(StartDateChanged(target.value))
                        }
                        onBlurFunction = { event ->
                            sendEvent(UpdateElectionStartDateRequest(credentials, electionName, start))
                        }
                    }
                }
                +"End:"
                input {
                    attrs {
                        placeholder = "YYYY-MM-DD HH:MM"
                        value = end
                        onChangeFunction = { event ->
                            val target = event.target as HTMLInputElement
                            sendEvent(EndDateChanged(target.value))
                        }
                        onBlurFunction = { event ->
                            sendEvent(UpdateElectionEndDateRequest(credentials, electionName, end))
                        }
                    }
                }
            }
            span {
                input(type = InputType.checkBox) {
                    attrs {
                        defaultChecked = secretBallot
                        onChangeFunction = { event ->
                            val target = event.target as HTMLInputElement
                            sendEvent(UpdateElectionSecretBallotRequest(
                                    credentials, electionName, target.checked))
                        }
                    }
                }
                +"Secret Ballot"
            }
            a(href = "#") {
                +"Candidates ($candidateCount)"
                attrs {
                    onClickFunction = {
                        sendEvent(ListCandidatesRequest(credentials, electionName))
                    }
                }

            }
            a(href = "#") {
                +"Voters ($voterCount)"
                attrs {
                    onClickFunction = {
                        sendEvent(ListVotersRequest(credentials, electionName))
                    }
                }
            }
            if (status == EDITING) {
                button {
                    +"Lock for Editing"
                    attrs {
                        onClickFunction = {
                            sendEvent(DoneEditingRequest(credentials, electionName))
                        }
                        disabled = status != Election.ElectionStatus.EDITING
                    }
                }
            }
            if (status == PENDING_MANUAL) {
                button {
                    +"Start Now"
                    attrs {
                        onClickFunction = {
                            sendEvent(StartNowRequest(credentials, electionName))
                        }
                    }
                }
            }
            if (status == RUNNING_MANUAL) {
                button {
                    +"End Now"
                    attrs {
                        onClickFunction = {
                            sendEvent(EndNowRequest(credentials, electionName))
                        }
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
                      page: ElectionPage) = child(ElectionComponent::class) {
    attrs.sendEvent = sendEvent
    attrs.page = page
}
