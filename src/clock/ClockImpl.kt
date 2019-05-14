package clock

import kotlin.js.Date

class ClockImpl : Clock {
    override fun now(): Date = Date()
}