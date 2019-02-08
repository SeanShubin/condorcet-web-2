import org.w3c.dom.Document
import org.w3c.dom.Window

class EnvironmentImpl(override val document: Document,
                      override val window: Window) : Environment
