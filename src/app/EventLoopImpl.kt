package app

import effect.Effect
import event.Event
import model.State

class EventLoopImpl : EventLoop {
    override fun reactTo(state: State, event: Event): StateAndEffects {
        fun updateState(f: (State) -> State): StateAndEffects =
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
                is Event.NavRegisterRequest -> updateState {
                    state.navRegister()
                }
                is Event.NavHomeRequest -> updateState {
                    state.navHome()
                }
                is Event.NavElectionsRequest -> TODO()
                is Event.NavElectionRequest -> TODO()
                is Event.NavCandidatesRequest -> TODO()
                is Event.NavVotersRequest -> TODO()
                is Event.NavBallotsRequest -> TODO()
                is Event.NavBallotRequest -> TODO()
                is Event.NavPrototypeRequest -> updateState {
                    state.navPrototype()
                }
                is Event.Error -> updateState {
                    state.error(event.message)
                }
                else -> effects(Effect.Dispatch(Event.Error("unknown event $event")))
            }
        } catch (ex: Exception) {
            effects(Effect.Dispatch(Event.Error("exception: '${ex.message}'")))
        }
    }
}
