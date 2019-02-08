import org.w3c.dom.HTMLElement
import kotlin.dom.clear

class RendererImpl(private val root: HTMLElement,
                   private val renderableLookup: RenderableLookup) : Renderer {
    override fun render(model: Model, eventHandler: EventHandler) {
        root.clear()
        val renderable = renderableLookup.byName(model.currentPage)
        root.appendChild(renderable.render(eventHandler))
    }
}
