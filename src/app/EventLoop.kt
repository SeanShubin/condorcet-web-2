package app

import event.Event
import model.State

interface EventLoop {
    fun reactTo(state: State, event: Event): StateAndEffects
}