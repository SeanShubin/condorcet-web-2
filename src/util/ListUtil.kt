package util

object ListUtil {
    fun <T> List<T>.exactlyOne(): T =
            when (size) {
                1 -> get(0)
                else -> throw RuntimeException("Exactly 1 element expected, got $size")
            }

    fun <T> List<T>.nullOrOne(): T? =
            when (size) {
                0 -> null
                1 -> get(0)
                else -> throw RuntimeException("0 or 1 elements expected, got $size")
            }
}