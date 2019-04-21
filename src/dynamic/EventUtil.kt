package dynamic

object EventUtil {
    fun key(event: org.w3c.dom.events.Event): String {
        val keyboardEvent: dynamic = event
        return keyboardEvent.key as String
    }
}
