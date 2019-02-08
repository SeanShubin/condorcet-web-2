import kotlin.dom.clear

object EffectRender : Effect {
    override fun apply(environment: Environment, model: Model, renderableLookup: RenderableLookup, eventHandler: EventHandler) {
        val body = environment.document.body!!
        body.clear()
        val renderable = renderableLookup.byName(model.currentPage)
        body.appendChild(renderable.render(eventHandler))
    }
}
