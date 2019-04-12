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

    fun booleanToString(b: Boolean) = if (b) "yes" else "no"

    private fun pad2(s: Any): String = pad2(s.toString())
    private fun pad2(s: String): String = if (s.length < 2) {
        "0".repeat(2 - s.length) + s
    } else {
        s
    }
}
