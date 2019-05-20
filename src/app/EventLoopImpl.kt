package app

import effect.Effect
import event.CondorcetEvent
import event.CondorcetEvent.*
import pages.Page
import state.Model

class EventLoopImpl : EventLoop {
    override fun reactTo(model: Model, event: CondorcetEvent): StateAndEffects {
        val page = model.page
        console.log("event: $model $event")
        fun updatePage(f: (Page) -> Page): StateAndEffects =
                StateAndEffects(model.copy(page = f(page)), emptyList())
        fun effects(vararg effects: Effect): StateAndEffects =
                StateAndEffects(model, effects.toList())
        return try {
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
                is ListBallotsRequest -> effects(Effect.ListBallots(event.credentials))
                is ListBallotsSuccess -> updatePage {
                    page.navBallots(event.credentials, event.voterName, event.ballots)
                }
                is LoadElectionRequest -> effects(Effect.LoadElection(event.credentials, event.electionName))
                is LoadElectionSuccess -> updatePage {
                    page.navElection(event.credentials, event.election)
                }
                is UpdateStartDate -> effects(Effect.SetStartDate(event.credentials, event.electionName, event.startDate))
                is StartDateChanged -> updatePage {
                    page.startChanged(event.startDate)
                }
                is EndDateChanged -> updatePage {
                    page.endChanged(event.endDate)
                }
                is UpdateSecretBallot -> effects(Effect.SetSecretBallot(event.credentials, event.electionName, event.checked))
                else -> effects(Effect.Dispatch(Error("unknown event $event")))
            }
        } catch (ex: Exception) {
            effects(Effect.Dispatch(Error("exception: '${ex.message}'")))
        }
    }
}
