package app

import event.Event
import pages.Page

interface EventLoop {
    fun reactTo(state: Page, event: Event): StateAndEffects
}