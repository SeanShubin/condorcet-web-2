package model

import kotlin.js.Date

object StringConversions {
    fun dateToString(date: Date?) =
            if (date == null) ""
            else {
                val year = date.getFullYear().toString()
                val month = pad2(date.getMonth() + 1)
                val day = pad2(date.getDate())
                val hours = pad2(date.getHours())
                val minutes = pad2(date.getMinutes())
                "$year-$month-$day $hours:$minutes"
            }

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
