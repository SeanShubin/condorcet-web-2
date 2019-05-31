package conversion

import kotlin.js.Date

object StringConversions {
    fun Date?.toStringMinute() =
            if (this == null) ""
            else {
                val year = getFullYear().toString()
                val month = pad2(getMonth() + 1)
                val day = pad2(getDate())
                val hours = pad2(getHours())
                val minutes = pad2(getMinutes())
                "$year-$month-$day $hours:$minutes"
            }

    fun Date?.toStringSecond() =
            if (this == null) ""
            else {
                val year = getFullYear().toString()
                val month = pad2(getMonth() + 1)
                val day = pad2(getDate())
                val hours = pad2(getHours())
                val minutes = pad2(getMinutes())
                val seconds = pad2(getSeconds())
                "$year-$month-$day $hours:$minutes:$seconds"
            }

    fun String.clean(): String = this.trim().replace(Regex("""\s+"""), " ")

    fun String.toLines(): List<String> = this.split("\r\n", "\r", "\n")

    fun List<String>.clean(): List<String> = this.map { it.clean() }.filter { !it.isBlank() }

    private val nonDigitRegex = Regex("""[^\d]+""")

    fun stringToDate(asString: String, defaultDate: Date): Date {
        val parts = asString.split(nonDigitRegex)
        val unvalidatedYear = atIndexOrDefault(parts, 0, defaultDate.getFullYear())
        val year = if (unvalidatedYear < defaultDate.getFullYear()) {
            defaultDate.getFullYear()
        } else unvalidatedYear
        val month = atIndexOrDefault(parts, 1, defaultDate.getMonth() + 1)
        val day = atIndexOrDefault(parts, 2, defaultDate.getDate())
        val hour = atIndexOrDefault(parts, 3, 0)
        val minute = atIndexOrDefault(parts, 4, 0)
        val date = Date(year, month - 1, day, hour, minute)
        return date
    }

    private fun atIndexOrDefault(list: List<String>, index: Int, default: Int): Int =
            if (index < list.size) try {
                list[index].toInt()
            } catch (ex: NumberFormatException) {
                default
            }
            else default

    fun booleanToString(b: Boolean) = if (b) "yes" else "no"

    private fun pad2(s: Any): String = pad2(s.toString())
    private fun pad2(s: String): String = if (s.length < 2) {
        "0".repeat(2 - s.length) + s
    } else {
        s
    }
}
