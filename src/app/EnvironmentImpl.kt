package app

import api.Api
import effect.Effect
import event.Event
import model.State

class EnvironmentImpl : Environment {
    override fun handleEffect(state: State, handleEvent: (Event) -> Unit, api: Api, effect: Effect) {
        when (effect) {
            is Effect.Dispatch -> {
                handleEvent(effect.event)
            }
            is Effect.Login -> {
                api.login(effect.nameOrEmail, effect.password).then {
                    console.log("login success")
                    handleEvent(Event.NavHomeRequest)
                }.catch { throwable ->
                    console.log("login failure")
                    handleEvent(Event.Error(throwable.message ?: "<no message>"))
                }
            }
        }
    }
}
