package model

import model.StringConversions.booleanToString
import model.StringConversions.dateToString
import kotlin.js.Date

data class Election(val ownerName: String,
                    val name: String,
                    val end: Date? = null,
                    val secretBallot: Boolean = true,
                    val status: ElectionStatus = ElectionStatus.EDITING,
                    val candidateCount: Int = 0,
                    val voterCount: Int = 0) {
    val endString get() = dateToString(end)
    val secretBallotString get() = booleanToString(secretBallot)

    fun startNow() = copy(status = status.start(this))
    fun endNow() = copy(status = status.end(this))

    enum class ElectionStatus(val description: String) {
        EDITING("Editing") {
            override fun start(election: Election): ElectionStatus = LIVE
        },
        LIVE("Live") {
            override fun end(election: Election): ElectionStatus = CONCLUDED
        },
        CONCLUDED("Concluded");

        open fun start(election: Election): ElectionStatus =
                throw RuntimeException("Unsupported transition start() from ${this.name}")

        open fun end(election: Election): ElectionStatus =
                throw RuntimeException("Unsupported transition end() from ${this.name}")

        override fun toString(): String = this.name.toLowerCase()
    }
}
