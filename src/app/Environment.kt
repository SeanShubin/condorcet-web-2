package app

import api.Api
import effect.Effect
import model.State

interface Environment {
    fun handleEffect(state: State, eventLoop: EventLoop, api: Api, effect: Effect)
}
