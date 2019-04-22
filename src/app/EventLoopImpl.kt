package app

import effect.Effect
import event.Event
import pages.Page
import state.Model

class EventLoopImpl : EventLoop {
    override fun reactTo(model: Model, event: Event): StateAndEffects {
        val (page, pathName) = model
        console.log("event: $model $event")
        fun updateModel(f: (Model) -> Model): StateAndEffects =
                StateAndEffects(f(model), emptyList())

        fun updatePage(f: (Page) -> Page): StateAndEffects =
                StateAndEffects(model.copy(page = f(page)), emptyList())
        fun effects(vararg effects: Effect): StateAndEffects =
                StateAndEffects(model, effects.toList())
        return try {
            when (event) {
                is Event.NavLoginRequest -> updatePage {
                    page.navLogin()
                }
                is Event.LoginRequest -> effects(Effect.Login(event.nameOrEmail, event.password))
                is Event.LoginSuccess -> effects(Effect.Dispatch(Event.NavHomeRequest))
                is Event.LoginFailure -> updatePage {
                    page.loginFailure(event.message)
                }
                is Event.RegisterRequest -> effects(Effect.Register(
                        event.name, event.email, event.password, event.confirmPassword))
                is Event.RegisterFailure -> updatePage {
                    page.registerFailure(event.message)
                }
                is Event.NavRegisterRequest -> updatePage {
                    page.navRegister()
                }
                is Event.NavHomeRequest -> updatePage {
                    page.navHome()
                }
                is Event.NavPrototypeRequest -> updatePage {
                    page.navPrototype()
                }
                is Event.Error -> updatePage {
                    page.navError(event.message)
                }
                is Event.PathNameChanged -> {
                    val newState = model.copy(pathName = event.pathName)
                    val effect: Effect = when (pathName) {
                        "/login" -> Effect.Dispatch(Event.NavLoginRequest)
                        "/register" -> Effect.Dispatch(Event.NavRegisterRequest)
                        else -> Effect.SetPathName("/login")
                    }
                    StateAndEffects(newState, listOf(effect))
                }
                else -> effects(Effect.Dispatch(Event.Error("unknown event $event")))
            }
        } catch (ex: Exception) {
            effects(Effect.Dispatch(Event.Error("exception: '${ex.message}'")))
        }
    }
}
