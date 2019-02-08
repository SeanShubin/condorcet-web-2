import MyHtmlApi.createLink
import MyHtmlApi.createParagraph
import MyHtmlApi.wrapInDivWithClass
import org.w3c.dom.HTMLElement
import kotlin.browser.window

class Debug : Renderable {
    override fun render(eventHandler: EventHandler): HTMLElement {
        val length = createParagraph("Items in local storage = ${window.localStorage.length}")
        val entries = (0 until window.localStorage.length).map { i ->
            val key = window.localStorage.key(i) ?: "<null>"
            val value = window.localStorage.getItem(key) ?: "<null>"
            createParagraph("$key = $value")
        }
        val logout = createLink("logout")
        logout.onclick = { _ ->
            eventHandler.handleEvent(EventLogout)
        }
        val list = listOf(length) + entries + listOf(logout)
        return wrapInDivWithClass(list, "single-column-flex")
    }
}
