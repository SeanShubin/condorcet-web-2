package effect

import event.Event

interface Effect {
    data class Login(val nameOrEmail: String, val password: String) : Effect
    data class Dispatch(val event: Event) : Effect
}
