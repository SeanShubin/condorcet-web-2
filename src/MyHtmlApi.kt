import org.w3c.dom.*
import kotlin.browser.document
import kotlin.dom.addClass

object MyHtmlApi {
    fun createInput(placeholder:String? = null):HTMLInputElement {
        val input = document.createElement("input") as HTMLInputElement
        if(placeholder != null) input.placeholder = placeholder
        return input
    }
    fun createDiv():HTMLDivElement = document.createElement("div") as HTMLDivElement
    fun createParagraph(text:String):HTMLParagraphElement {
        val paragraph = document.createElement("p") as HTMLParagraphElement
        paragraph.textContent = text
        return paragraph
    }
    fun createPassword(placeholder:String? = null):HTMLInputElement {
        val password = createInput(placeholder = placeholder)
        password.type = "password"
        return password
    }
    fun createButton(caption:String):HTMLButtonElement {
        val button = document.createElement("button") as HTMLButtonElement
        button.type = "button"
        button.textContent = caption
        return button
    }
    fun createLink(caption:String):HTMLAnchorElement {
        val link = document.createElement("a") as HTMLAnchorElement
        link.textContent = caption
        link.href = "#"
        return link
    }
    fun createHeader(caption:String):HTMLElement {
        val element = document.createElement("h1") as HTMLElement
        element.textContent = caption
        return element
    }
    fun appendChildren(element:HTMLElement, children:List<HTMLElement>){
        children.forEach{element.append(it)}
    }
    fun wrapInDivWithClass(children:List<HTMLElement>, className:String):HTMLDivElement{
        val div = createDiv()
        appendChildren(div, children)
        div.addClass(className)
        return div
    }
}
