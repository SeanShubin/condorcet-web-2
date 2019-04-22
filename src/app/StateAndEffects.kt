package app

import effect.Effect
import state.Model

data class StateAndEffects(val state: Model, val effects: List<Effect>)
