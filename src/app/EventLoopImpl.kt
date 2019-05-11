package app

import effect.Effect
import event.Event
import pages.Page
import state.Model

class EventLoopImpl : EventLoop {
    override fun reactTo(model: Model, event: Event): StateAndEffects {
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
                is Event.NavLoginRequest -> navigate(page.navLogin(), "/login")
                is Event.LoginRequest -> effects(Effect.Login(event.nameOrEmail, event.password))
                is Event.LoginSuccess -> effects(Effect.Dispatch(Event.NavHomeRequest(event.credentials)))
                is Event.LoginFailure -> updatePage {
                    page.loginFailure(event.message)
                }
                is Event.RegisterRequest -> effects(Effect.Register(
                        event.name,
                        event.email,
                        event.password,
                        event.confirmPassword))
                is Event.RegisterFailure -> updatePage {
                    page.registerFailure(event.message)
                }
                is Event.NavRegisterRequest -> navigate(page.navRegister(), "/register")
                is Event.NavHomeRequest -> updatePage {
                    page.navHome(event.credentials)
                }
                is Event.NavPrototypeRequest -> updatePage {
                    page.navPrototype()
                }
                is Event.Error -> updatePage {
                    page.navError(event.message)
                }
                is Event.LogoutRequest -> updatePage {
                    page.navLogin()
                }
                is Event.ListElectionsRequest -> effects(Effect.ListElections(event.credentials))
                is Event.ListElectionsSuccess -> updatePage {
                    page.navElections(event.credentials, event.elections)
                }
                is Event.CreateElectionRequest ->
                    effects(Effect.CreateElection(event.credentials, event.electionName))
                is Event.CreateElectionSuccess -> updatePage {
                    page.navElection(event.credentials, event.election)
                }
                else -> effects(Effect.Dispatch(Event.Error("unknown event $event")))
            }
        } catch (ex: Exception) {
            effects(Effect.Dispatch(Event.Error("exception: '${ex.message}'")))
        }
    }
}
