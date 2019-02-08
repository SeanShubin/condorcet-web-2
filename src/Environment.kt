import org.w3c.dom.Document
import org.w3c.dom.Window

interface Environment {
    val document: Document
    val window: Window
}
