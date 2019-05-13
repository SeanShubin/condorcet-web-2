package app

import effect.Effect
import event.CondorcetEvent
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
                is CondorcetEvent.NavLoginRequest -> navigate(page.navLogin(), "/login")
                is CondorcetEvent.LoginRequest -> effects(Effect.Login(event.nameOrEmail, event.password))
                is CondorcetEvent.LoginSuccess -> effects(Effect.Dispatch(CondorcetEvent.NavHomeRequest(event.credentials)))
                is CondorcetEvent.LoginFailure -> updatePage {
                    page.loginFailure(event.message)
                }
                is CondorcetEvent.RegisterRequest -> effects(Effect.Register(
                        event.name,
                        event.email,
                        event.password,
                        event.confirmPassword))
                is CondorcetEvent.RegisterFailure -> updatePage {
                    page.registerFailure(event.message)
                }
                is CondorcetEvent.NavRegisterRequest -> navigate(page.navRegister(), "/register")
                is CondorcetEvent.NavHomeRequest -> updatePage {
                    page.navHome(event.credentials)
                }
                is CondorcetEvent.NavPrototypeRequest -> updatePage {
                    page.navPrototype()
                }
                is CondorcetEvent.Error -> updatePage {
                    page.navError(event.message)
                }
                is CondorcetEvent.LogoutRequest -> updatePage {
                    page.navLogin()
                }
                is CondorcetEvent.ListElectionsRequest -> effects(Effect.ListElections(event.credentials))
                is CondorcetEvent.ListElectionsSuccess -> updatePage {
                    page.navElections(event.credentials, event.elections)
                }
                is CondorcetEvent.CreateElectionRequest ->
                    effects(Effect.CreateElection(event.credentials, event.electionName))
                is CondorcetEvent.CreateElectionSuccess -> updatePage {
                    page.navElection(event.credentials, event.election)
                }
                is CondorcetEvent.ListBallotsRequest -> effects(Effect.ListBallots(event.credentials))
                is CondorcetEvent.ListBallotsSuccess -> updatePage {
                    page.navBallots(event.credentials, event.voterName, event.ballots)
                }
                else -> effects(Effect.Dispatch(CondorcetEvent.Error("unknown event $event")))
            }
        } catch (ex: Exception) {
            effects(Effect.Dispatch(CondorcetEvent.Error("exception: '${ex.message}'")))
        }
    }
}
