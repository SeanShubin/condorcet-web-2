package app

import api.Api
import effect.Effect
import event.Event
import model.State

interface Environment {
    fun handleEffect(state: State, handleEvent: (Event) -> Unit, api: Api, effect: Effect)
}
