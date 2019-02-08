import MyHtmlApi.createInput
import MyHtmlApi.createPassword
import MyHtmlApi.createButton
import MyHtmlApi.createHeader
import MyHtmlApi.createLink
import MyHtmlApi.wrapInDivWithClass
import org.w3c.dom.HTMLElement
import kotlin.browser.window

class Login:Renderable{
    override fun render(eventHandler: EventHandler):HTMLElement {
        val header = createHeader("Login")
        val name = createInput(placeholder = "name or email")
        val password = createPassword(placeholder = "password")
        val button = createButton("Login")
        val register = createLink("register")
        button.onclick = { _ ->
            eventHandler.handleEvent(EventLogin(nameOrEmail = name.value, password=  password.value))
        }
        register.onclick = {_ ->
            eventHandler.handleEvent(NavigateTo("register"))
        }
        val list = listOf(header, name, password, button, register)
        return wrapInDivWithClass(list, "single-column-flex")
    }
}
