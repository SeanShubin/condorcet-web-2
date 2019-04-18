package app

import effect.Effect
import model.State

data class StateAndEffects(val state: State, val effects: List<Effect>)
