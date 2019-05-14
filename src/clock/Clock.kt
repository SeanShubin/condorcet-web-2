package clock

import kotlin.js.Date

interface Clock {
    fun now(): Date
}
