package effect

import app.Environment
import event.CondorcetEvent
import event.CondorcetEvent.*
import model.Credentials
import model.StringConversions.stringToDate

interface Effect {
    fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment)
    data class Dispatch(val event: CondorcetEvent) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            handleEvent(event)
        }
    }

    data class Login(val nameOrEmail: String, val password: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.login(nameOrEmail, password).then { credentials ->
                handleEvent(NavHomeRequest(credentials))
            }.catch { throwable ->
                handleEvent(LoginFailure(throwable.message ?: "Unable to login"))
            }
        }
    }
    data class Register(val name: String,
                        val email: String,
                        val password: String,
                        val confirmPassword: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            if (password != confirmPassword) {
                handleEvent(RegisterFailure("password does not match confirmation"))
            } else {
                environment.api.register(name, email, password).then { credentials ->
                    handleEvent(NavHomeRequest(credentials))
                }.catch { throwable ->
                    handleEvent(RegisterFailure(throwable.message ?: "Unable to register"))
                }
            }
        }
    }

    data class ListElections(val credentials: Credentials) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.listElections(credentials).then { elections ->
                handleEvent(ListElectionsSuccess(credentials, elections))
            }.catch { throwable ->
                handleEvent(ListElectionsFailure(throwable.message ?: "Unable to list elections"))
            }
        }
    }

    data class CreateElection(val credentials: Credentials, val electionName: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.createElection(credentials, electionName).then { election ->
                handleEvent(CreateElectionSuccess(credentials, election))
            }.catch { throwable ->
                handleEvent(CreateElectionFailure(throwable.message ?: "Unable to create election"))
            }
        }
    }

    data class CopyElection(val credentials: Credentials, val newElectionName: String, val electionToCopyName: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.copyElection(credentials, newElectionName, electionToCopyName).then { election ->
                handleEvent(CreateElectionSuccess(credentials, election))
            }.catch { throwable ->
                handleEvent(CreateElectionFailure(throwable.message ?: "Unable to copy election $electionToCopyName"))
            }
        }
    }

    data class ListBallots(val credentials: Credentials) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.listBallots(credentials, credentials.name).then { ballots ->
                handleEvent(ListBallotsSuccess(credentials, credentials.name, ballots))
            }.catch { throwable ->
                handleEvent(ListBallotsFailure(throwable.message ?: "Unable to list ballots"))
            }
        }
    }

    data class LoadElection(val credentials: Credentials, val electionName: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.getElection(credentials, electionName).then { election ->
                handleEvent(LoadElectionSuccess(credentials, election))
            }.catch { throwable ->
                handleEvent(LoadElectionFailure(throwable.message ?: "Unable to load election $electionName"))
            }
        }
    }

    data class SetStartDate(val credentials: Credentials, val electionName: String, val startDateString: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            val now = environment.clock.now()
            val isoStartDate = if (startDateString.isBlank()) null else stringToDate(startDateString, now).toISOString()
            environment.api.setStartDate(credentials, electionName, isoStartDate).then { election ->
                handleEvent(LoadElectionSuccess(credentials, election))
            }.catch { throwable ->
                handleEvent(LoadElectionFailure(throwable.message ?: "Unable to set start date for $electionName"))
            }
        }
    }

    data class SetEndDate(val credentials: Credentials, val electionName: String, val endDateString: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            val now = environment.clock.now()
            val isoEndDate = if (endDateString.isBlank()) null else stringToDate(endDateString, now).toISOString()
            environment.api.setEndDate(credentials, electionName, isoEndDate).then { election ->
                handleEvent(LoadElectionSuccess(credentials, election))
            }.catch { throwable ->
                handleEvent(LoadElectionFailure(throwable.message ?: "Unable to set end date for $electionName"))
            }
        }
    }

    data class SetSecretBallot(val credentials: Credentials, val electionName: String, val secretBallot: Boolean) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.setSecretBallot(credentials, electionName, secretBallot).then { election ->
                handleEvent(LoadElectionSuccess(credentials, election))
            }.catch { throwable ->
                handleEvent(LoadElectionFailure(throwable.message
                        ?: "Unable to set secretBallot to $secretBallot for $electionName"))
            }
        }
    }

    data class ListCandidates(val credentials: Credentials, val electionName: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.listCandidates(credentials, electionName).then { candidates ->
                handleEvent(ListCandidatesSuccess(credentials, electionName, candidates))
            }.catch { throwable ->
                handleEvent(ListCandidatesFailure(throwable.message ?: "Unable to list candidates for $electionName"))
            }
        }
    }

    data class ListVoters(val credentials: Credentials, val electionName: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.listEligibleVoters(credentials, electionName).then { voters ->
                handleEvent(ListVotersSuccess(credentials, electionName, voters.list, voters.isAllVoters))
            }.catch { throwable ->
                handleEvent(ListVotersFailure(throwable.message ?: "Unable to list eligible voters for $electionName"))
            }
        }
    }

    data class DoneEditing(val credentials: Credentials, val electionName: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.doneEditingElection(credentials, electionName).then { election ->
                handleEvent(LoadElectionSuccess(credentials, election))
            }.catch { throwable ->
                handleEvent(LoadElectionFailure(throwable.message
                        ?: "Unable to lock election $electionName for further edits"))
            }
        }
    }

    data class StartElectionNow(val credentials: Credentials, val electionName: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.startElection(credentials, electionName).then { election ->
                handleEvent(LoadElectionSuccess(credentials, election))
            }.catch { throwable ->
                handleEvent(LoadElectionFailure(throwable.message
                        ?: "Unable to start election $electionName manually"))
            }
        }
    }

    data class EndElectionNow(val credentials: Credentials, val electionName: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.endElection(credentials, electionName).then { election ->
                handleEvent(LoadElectionSuccess(credentials, election))
            }.catch { throwable ->
                handleEvent(LoadElectionFailure(throwable.message
                        ?: "Unable to end election $electionName manually"))
            }
        }
    }

    data class SetCandidates(val credentials: Credentials,
                             val electionName: String,
                             val candidates: List<String>) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.updateCandidates(credentials, electionName, candidates).then { candidates ->
                handleEvent(ListCandidatesSuccess(credentials, electionName, candidates))
            }.catch { throwable ->
                handleEvent(ListCandidatesFailure(throwable.message
                        ?: "Unable to list candidates for $electionName"))
            }
        }
    }

    data class SetVoters(val credentials: Credentials,
                         val electionName: String,
                         val voters: List<String>) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.updateEligibleVoters(credentials, electionName, voters).then { voters ->
                handleEvent(ListVotersSuccess(credentials, electionName, voters.list, voters.isAllVoters))
            }.catch { throwable ->
                handleEvent(ListVotersFailure(throwable.message
                        ?: "Unable to list eligible voters for $electionName"))
            }
        }
    }

    data class SetVotersToAll(val credentials: Credentials,
                              val electionName: String) : Effect {
        override fun apply(handleEvent: (CondorcetEvent) -> Unit, environment: Environment) {
            environment.api.updateEligibleVotersToAll(credentials, electionName).then { voters ->
                handleEvent(ListVotersSuccess(credentials, electionName, voters.list, voters.isAllVoters))
            }.catch { throwable ->
                handleEvent(ListVotersFailure(throwable.message
                        ?: "Unable to list eligible voters for $electionName"))
            }
        }
    }
}
