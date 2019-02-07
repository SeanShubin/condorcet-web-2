import kotlin.browser.window

object EventHandler {
    fun handleEvent(event: EventInterface) {
        val oldState = window.localStorage.getItem("state")
        val oldModel = Model.fromState(oldState)
        val (newModel, effects) = Reactor.react(oldModel, event)
        val newState = newModel.toState()
        window.localStorage.setItem("state", newState)
        effects.forEach {
            EffectHandler.handleEffect(it)
        }
    }
}
