package model

import model.StringConversions.booleanToString
import model.StringConversions.dateToString
import kotlin.js.Date

data class Election(val owner: String,
                    val name: String,
                    val start: Date? = null,
                    val end: Date? = null,
                    val secretBallot: Boolean = true,
                    val status: ElectionStatus = ElectionStatus.EDITING) {
    val startString get() = dateToString(start)
    val endString get() = dateToString(end)
    val secretBallotString get() = booleanToString(secretBallot)

    enum class ElectionStatus {
        EDITING, // may still change, not started yet
        PENDING, // closed for changes, not started yet
        RUNNING, // ballots may be cast
        CONCLUDED; // election is over

        override fun toString(): String = this.name.toLowerCase()
    }
}
