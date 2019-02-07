import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.browser.window

fun main() {
    val body: HTMLElement = document.body!!
    val pageName = window.localStorage.getItem("pageName")
    val loginPage = Login()
    val registerPage = Register()
    val debugPage = Debug()
    val pages:Map<String, Renderable> = mapOf(
            Pair("login", loginPage),
            Pair("register", registerPage),
            Pair("debug", debugPage))
    val page = pages[pageName] ?: loginPage
    body.appendChild(page.render())
    body.appendChild(debugPage.render())
}
