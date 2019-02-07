import kotlin.dom.clear

object EffectRender:Effect {
    override fun apply(model:Model, environment:Environment){
        environment.body.clear()
        val renderable = RenderableLookup.fromModel(model)
        environment.body.appendChild(renderable.render())
    }
}
