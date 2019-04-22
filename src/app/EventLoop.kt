package app

import event.Event
import state.Model

interface EventLoop {
    fun reactTo(model: Model, event: Event): StateAndEffects
}