package app

import api.Api
import effect.Effect
import event.Event
import pages.Page

interface Environment {
    fun handleEffect(state: Page, handleEvent: (Event) -> Unit, api: Api, effect: Effect)
}
