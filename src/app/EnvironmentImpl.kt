package app

import api.Api
import effect.Effect
import event.Event
import model.State

class EnvironmentImpl : Environment {
    override fun handleEffect(state: State, eventLoop: EventLoop, api: Api, effect: Effect) {
        when (effect) {
            is Effect.Dispatch -> {
                eventLoop.reactTo(state, effect.event)
            }
            is Effect.Login -> {
                api.login(effect.nameOrEmail, effect.password).then {
                    console.log("login success")
                    eventLoop.reactTo(state, Event.NavHomeRequest)
                }.catch { throwable ->
                    console.log("login failure")
                    eventLoop.reactTo(state, Event.Error(throwable.message ?: "<no message>"))
                }
            }
        }
    }
}
