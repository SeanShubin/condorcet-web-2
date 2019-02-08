import kotlin.dom.clear

class EffectRender(private val renderable:Renderable):Effect {
    override fun apply(environment:Environment, eventHandler:EventHandler){
        environment.body.clear()
        environment.body.appendChild(renderable.render(eventHandler))
    }
}
