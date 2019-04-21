package app

import effect.Effect
import event.Event
import pages.Page

class EventLoopImpl : EventLoop {
    override fun reactTo(state: Page, event: Event): StateAndEffects {
        console.log("event: $state $event")
        fun updateState(f: (Page) -> Page): StateAndEffects =
                StateAndEffects(f(state), emptyList())

        fun effects(vararg effects: Effect): StateAndEffects =
                StateAndEffects(state, effects.toList())
        return try {
            when (event) {
                is Event.NavLoginRequest -> updateState {
                    state.navLogin()
                }
                is Event.LoginRequest -> effects(Effect.Login(event.nameOrEmail, event.password))
                is Event.LoginSuccess -> effects(Effect.Dispatch(Event.NavHomeRequest))
                is Event.LoginFailure -> updateState {
                    state.loginFailure(event.message)
                }
                is Event.RegisterRequest -> effects(Effect.Register(
                        event.name, event.email, event.password, event.confirmPassword))
                is Event.RegisterFailure -> updateState {
                    state.registerFailure(event.message)
                }
                is Event.NavRegisterRequest -> updateState {
                    state.navRegister()
                }
                is Event.NavHomeRequest -> updateState {
                    state.navHome()
                }
                is Event.NavPrototypeRequest -> updateState {
                    state.navPrototype()
                }
                is Event.Error -> updateState {
                    state.navError(event.message)
                }
                else -> effects(Effect.Dispatch(Event.Error("unknown event $event")))
            }
        } catch (ex: Exception) {
            effects(Effect.Dispatch(Event.Error("exception: '${ex.message}'")))
        }
    }
}
