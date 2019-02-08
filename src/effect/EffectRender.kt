object EffectRender : Effect {
    override fun apply(environment: Environment, model: Model, renderer: Renderer, eventHandler: EventHandler) {
        renderer.render(model, eventHandler)
    }
}
