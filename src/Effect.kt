interface Effect{
    fun apply(environment: Environment, model: Model, renderer: Renderer, eventHandler: EventHandler)
}
