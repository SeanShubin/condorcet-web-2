import MyHtmlApi.createButton
import MyHtmlApi.createHeader
import MyHtmlApi.createInput
import MyHtmlApi.createLink
import MyHtmlApi.createPassword
import MyHtmlApi.wrapInDivWithClass
import org.w3c.dom.HTMLElement
import kotlin.browser.window

class Register : Renderable {
    override fun render(eventHandler: EventHandler): HTMLElement {
        val header = createHeader("Register")
        val name = createInput(placeholder = "name")
        val email = createInput(placeholder = "email")
        val password = createPassword(placeholder = "password")
        val confirmPassword = createPassword(placeholder = "confirm password")
        val register = createButton("Register")
        val login = createLink("login")
        register.onclick = { _ -> window.localStorage.setItem("pageName", "login") }
        login.onclick = { _ -> window.localStorage.setItem("pageName", "login") }
        val list = listOf(header, name, email, password, confirmPassword, register, login)
        return wrapInDivWithClass(list, "single-column-flex")
    }
}
