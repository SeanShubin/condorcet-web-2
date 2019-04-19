package model

import model.StringConversions.booleanToString
import model.StringConversions.dateToString
import kotlin.js.Date

data class Election(val ownerName: String,
                    val name: String,
                    val start: Date? = null,
                    val end: Date? = null,
                    val secretBallot: Boolean = true,
                    val status: ElectionStatus = ElectionStatus.EDITING,
                    val candidateCount: Int = 0,
                    val voterCount: Int = 0) {
    val startString get() = dateToString(start)
    val endString get() = dateToString(end)
    val secretBallotString get() = booleanToString(secretBallot)

    enum class ElectionStatus(val description: String) {
        EDITING("Editing, will not go live"),
        PENDING_SCHEDULE("Locked for edits, will go live at scheduled time"),
        PENDING_MANUAL("Locked for edits, must be manually started"),
        RUNNING_SCHEDULE("Live, will end at scheduled time"),
        RUNNING_MANUAL("Live, must be manually closed"),
        CONCLUDED("Concluded");

        override fun toString(): String = this.name.toLowerCase()
    }
}
