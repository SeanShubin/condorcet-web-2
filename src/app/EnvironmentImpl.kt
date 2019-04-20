package app

import api.Api
import effect.Effect
import event.Event
import pages.Page

class EnvironmentImpl : Environment {
    override fun handleEffect(state: Page, handleEvent: (Event) -> Unit, api: Api, effect: Effect) {
        console.log("effect: $state $effect")
        when (effect) {
            is Effect.Dispatch -> {
                handleEvent(effect.event)
            }
            is Effect.Login -> {
                api.login(effect.nameOrEmail, effect.password).then {
                    handleEvent(Event.NavHomeRequest)
                }.catch { throwable ->
                    handleEvent(Event.LoginFailure(throwable.message ?: "<no message>"))
                }
            }
        }
    }
}
