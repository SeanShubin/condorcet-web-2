package effect

import event.Event

interface Effect {
    data class Dispatch(val event: Event) : Effect
    data class Login(val nameOrEmail: String, val password: String) : Effect
    data class Register(val name: String,
                        val email: String,
                        val password: String,
                        val confirmPassword: String) : Effect
}
