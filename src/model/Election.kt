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

    fun doneEditing() = copy(status = status.done(this))

    enum class ElectionStatus(val description: String) {
        EDITING("Editing, will not go live") {
            override fun done(election: Election): ElectionStatus =
                    if (election.start == null) {
                        PENDING_MANUAL
                    } else {
                        PENDING_SCHEDULE
                    }
        },
        PENDING_SCHEDULE("Locked for edits, will go live at scheduled time"),
        PENDING_MANUAL("Locked for edits, must be manually started"),
        RUNNING_SCHEDULE("Live, will end at scheduled time"),
        RUNNING_MANUAL("Live, must be manually closed"),
        CONCLUDED("Concluded");

        open fun done(election: Election): ElectionStatus = throw RuntimeException("Unsupported transition done() from ${this.name}")

        override fun toString(): String = this.name.toLowerCase()
    }
}
