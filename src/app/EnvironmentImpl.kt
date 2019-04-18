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
                    eventLoop.reactTo(state, Event.NavHomeRequest)
                }.catch { throwable ->
                    eventLoop.reactTo(state, Event.Error(throwable.message ?: "<no message>"))
                }
            }
        }
    }
}
