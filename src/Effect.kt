interface Effect{
    fun apply(environment: Environment, model: Model, renderableLookup: RenderableLookup, eventHandler: EventHandler)
}
