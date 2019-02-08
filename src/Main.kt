import kotlin.browser.document
import kotlin.browser.window

fun main() {
    val environment = EnvironmentImpl(document, window)
    val modelRepository = ModelRepositoryImpl(environment)
    val renderableLookup = RenderableLookupImpl()
    val reactor = ReactorImpl()
    val renderer = RendererImpl(environment.document.body!!, renderableLookup)
    val eventHandler = EventHandlerImpl(modelRepository, environment, reactor, renderer)
    eventHandler.handleEvent(EventInitialize)
}
