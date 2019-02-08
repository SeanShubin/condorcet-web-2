import kotlin.dom.clear

class EffectRender(private val renderable:Renderable):Effect {
    override fun apply(environment:Environment, eventHandler:EventHandler){
        val body = environment.document.body!!
        body.clear()
        body.appendChild(renderable.render(eventHandler))
    }
}
