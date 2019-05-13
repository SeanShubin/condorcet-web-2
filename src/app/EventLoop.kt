package app

import event.CondorcetEvent
import state.Model

interface EventLoop {
    fun reactTo(model: Model, event: CondorcetEvent): StateAndEffects
}