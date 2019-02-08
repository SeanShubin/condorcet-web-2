import org.w3c.dom.HTMLElement
import kotlin.browser.document

fun main() {
    val body: HTMLElement = document.body!!
    val modelRepository = ModelRepositoryImpl
    val environment = EnvironmentImpl(body)
    val renderableLookup = RenderableLookupImpl()
    val reactor = ReactorImpl(renderableLookup)
    val eventHandler = EventHandlerImpl(modelRepository, environment, reactor)
    eventHandler.handleEvent(EventInitialize)
}
