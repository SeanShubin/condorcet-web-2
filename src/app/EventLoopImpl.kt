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

        fun navigate(page: Page, location: String): StateAndEffects =
                StateAndEffects(
                        model.copy(page = page),
                        listOf(Effect.SetPathName(location)))
        return try {
            when (event) {
                is NavLoginRequest -> navigate(page.navLogin(), "/login")
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
                is NavRegisterRequest -> navigate(page.navRegister(), "/register")
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
                else -> effects(Effect.Dispatch(Error("unknown event $event")))
            }
        } catch (ex: Exception) {
            effects(Effect.Dispatch(Error("exception: '${ex.message}'")))
        }
    }
}
