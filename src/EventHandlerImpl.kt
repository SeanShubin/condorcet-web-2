class EventHandlerImpl(private val modelRepository: ModelRepository,
                       private val environment:Environment,
                       private val reactor: Reactor,
                       private val renderableLookup: RenderableLookup) : EventHandler {
    override fun handleEvent(event: EventInterface) {
        val oldModel = modelRepository.model
        val (newModel, effects) = reactor.react(oldModel, event)
        modelRepository.model = newModel
        effects.forEach {
            it.apply(environment, newModel, renderableLookup, this)
        }
    }
}
