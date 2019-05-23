package app

import effect.Effect
import event.CondorcetEvent
import event.CondorcetEvent.*
import pages.Page
import state.Model

class EventLoopImpl : EventLoop {
    override fun reactTo(model: Model, event: CondorcetEvent): StateAndEffects {
        val page = model.page
        //        console.log("event: $event")
//        console.log("old model: $model")
        fun updatePage(f: (Page) -> Page): StateAndEffects =
                StateAndEffects(model.copy(page = f(page)), emptyList())
        fun effects(vararg effects: Effect): StateAndEffects =
                StateAndEffects(model, effects.toList())

        val stateAndEffects = try {
            when (event) {
                is NavLoginRequest -> updatePage {
                    page.navLogin()
                }
                is LoginRequest -> effects(Effect.Login(event.nameOrEmail, event.password))
                is LoginSuccess -> effects(Effect.Dispatch(NavHomeRequest(event.credentials)))
                is LoginFailure -> updatePage {
                    page.loginFailure(event.message)
                }
                is RegisterRequest -> effects(Effect.Register(
                        event.name,
                        event.email,
                        event.password,
                        event.confirmPassword))
                is RegisterFailure -> updatePage {
                    page.registerFailure(event.message)
                }
                is NavRegisterRequest -> updatePage {
                    page.navRegister()
                }
                is NavHomeRequest -> updatePage {
                    page.navHome(event.credentials)
                }
                is NavPrototypeRequest -> updatePage {
                    page.navPrototype()
                }
                is Error -> updatePage {
                    page.navError(event.message)
                }
                is LogoutRequest -> updatePage {
                    page.navLogin()
                }
                is ListElectionsRequest -> effects(Effect.ListElections(event.credentials))
                is ListElectionsSuccess -> updatePage {
                    page.navElections(event.credentials, event.elections)
                }
                is CreateElectionRequest ->
                    effects(Effect.CreateElection(event.credentials, event.electionName))
                is CreateElectionSuccess -> updatePage {
                    page.navElection(event.credentials, event.election)
                }
                is CopyElectionRequest ->
                    effects(Effect.CopyElection(event.credentials, event.newElectionName, event.electionToCopyName))

                is ListBallotsRequest -> effects(Effect.ListBallots(event.credentials))
                is ListBallotsSuccess -> updatePage {
                    page.navBallots(event.credentials, event.voterName, event.ballots)
                }
                is LoadElectionRequest -> effects(Effect.LoadElection(event.credentials, event.electionName))
                is LoadElectionSuccess -> updatePage {
                    page.navElection(event.credentials, event.election)
                }
                is UpdateElectionStartDateRequest -> effects(Effect.SetStartDate(event.credentials, event.electionName, event.startDate))
                is UpdateElectionEndDateRequest -> effects(Effect.SetEndDate(event.credentials, event.electionName, event.endDate))
                is StartDateChanged -> updatePage {
                    page.startChanged(event.startDate)
                }
                is EndDateChanged -> updatePage {
                    page.endChanged(event.endDate)
                }
                is UpdateElectionSecretBallotRequest -> effects(Effect.SetSecretBallot(event.credentials, event.electionName, event.checked))
                is ListCandidatesRequest -> effects(Effect.ListCandidates(event.credentials, event.electionName))
                is ListCandidatesSuccess -> updatePage {
                    page.navCandidates(event.credentials, event.electionName, event.candidates)
                }
                is ListVotersRequest -> effects(Effect.ListVoters(event.credentials, event.electionName))
                is ListVotersSuccess -> updatePage {
                    page.navVoters(event.credentials, event.electionName, event.voters, event.isAllVoters)
                }
                is DoneEditingRequest -> effects(Effect.DoneEditing(event.credentials, event.electionName))
                is StartNowRequest -> effects(Effect.StartElectionNow(event.credentials, event.electionName))
                is EndNowRequest -> effects(Effect.EndElectionNow(event.credentials, event.electionName))
                is UpdateCandidatesRequest -> effects(Effect.SetCandidates(event.credentials, event.electionName, event.candidates))
                is UpdateVotersRequest -> effects(Effect.SetVoters(event.credentials, event.electionName, event.voters))
                is UpdateToAllVotersRequest -> effects(Effect.SetVotersToAll(event.credentials, event.electionName))
                else -> effects(Effect.Dispatch(Error("unknown event $event")))
            }
        } catch (ex: Exception) {
            effects(Effect.Dispatch(Error("exception: '${ex.message}'")))
        }
//        console.log("new model: ${stateAndEffects.state}")
        return stateAndEffects
    }
}
