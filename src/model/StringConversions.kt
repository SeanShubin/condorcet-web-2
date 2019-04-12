package model

import kotlin.js.Date

object StringConversions {
    fun dateToString(date: Date?) = date?.toUTCString() ?: ""
    fun booleanToString(b: Boolean) = if (b) "yes" else "no"
}
