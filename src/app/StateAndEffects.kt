package app

import effect.Effect
import pages.Page

data class StateAndEffects(val state: Page, val effects: List<Effect>)
