import MyHtmlApi.createHeader
import MyHtmlApi.createLink
import MyHtmlApi.wrapInDivWithClass
import org.w3c.dom.HTMLElement

class Home : Renderable {
    override fun render(eventHandler: EventHandler): HTMLElement {
        val header = createHeader("Condorcet")
        val logout = createLink("logout")
        logout.onclick = { _ ->
            eventHandler.handleEvent(EventLogout)
        }
        val list = listOf(header, logout)
        return wrapInDivWithClass(list, "single-column-flex")
    }
}
