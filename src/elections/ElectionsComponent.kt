package elections

import event.CondorcetEvent
import event.CondorcetEvent.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import model.Credentials
import model.Election
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*

interface ElectionsProps : RProps {
    var sendEvent: (CondorcetEvent) -> Unit
    var credentials: Credentials
    var elections: List<Election>
}

interface ElectionsState : RState {
    var electionName: String
    var electionToCopy: String
}

class ElectionsComponent : RComponent<ElectionsProps, ElectionsState>() {
    override fun RBuilder.render() {
        val sendEvent = props.sendEvent
        val credentials = props.credentials
        val elections = props.elections
        div(classes = "single-column-flex") {
            h1 { +"Elections" }
            div {
                input {
                    attrs {
                        placeholder = "election name"
                        onChangeFunction = { event ->
                            val target = event.target as HTMLInputElement
                            setState {
                                electionName = target.value
                            }
                        }
                    }
                }
                button {
                    +"Create"
                    attrs {
                        onClickFunction = {
                            sendEvent(CreateElectionRequest(credentials, state.electionName))
                        }
                    }
                }
            }
            div {
                input {
                    attrs {
                        placeholder = "election name"
                        onChangeFunction = { event ->
                            val target = event.target as HTMLInputElement
                            setState {
                                electionToCopy = target.value
                            }
                        }
                    }
                }
                select {
                    for (election in elections) {
                        option {
                            +election.name
                        }
                    }
                }
                button {
                    +"Copy"
                    attrs.onClickFunction = {
                        sendEvent(CopyElectionRequest(credentials, state.electionToCopy))
                    }
                }
            }
            table {
                thead {
                    tr {
                        th {
                            +"edit"
                        }
                        th {
                            +"owner"
                        }
                        th {
                            +"name"
                        }
                        th {
                            +"start"
                        }
                        th {
                            +"end"
                        }
                        th {
                            +"secret ballot"
                        }
                    }
                }
                tbody {
                    for (election in elections) {
                        tr {
                            td {
                                a(href = "#") {
                                    +"edit"
                                    attrs.onClickFunction = {
                                        sendEvent(LoadElectionRequest(credentials, election.name))
                                    }
                                }
                            }
                            td {
                                +election.ownerName
                            }
                            td {
                                +election.name
                            }
                            td {
                                +election.startString
                            }
                            td {
                                +election.endString
                            }
                            td {
                                +election.secretBallotString
                            }
                        }
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

fun RBuilder.elections(sendEvent: (CondorcetEvent) -> Unit, credentials: Credentials, elections: List<Election>) = child(ElectionsComponent::class) {
    attrs.sendEvent = sendEvent
    attrs.credentials = credentials
    attrs.elections = elections
}