package sample

import model.Ballot
import model.Election
import model.Election.ElectionStatus
import model.Ranking
import model.VoterAndBallots
import kotlin.js.Date

class Sample {
    private var index = 0
    fun electionName(): String {
        index++
        return "Election $index"
    }

    fun voterName(): String {
        index++
        return "Voter $index"
    }

    fun candidateName(): String {
        index++
        return "Candidate $index"
    }

    fun rank(): Int {
        index++
        return index
    }

    fun rankings(howMany: Int): List<Ranking> = ((1..howMany).map { ranking() })
    fun ranking(): Ranking =
            Ranking(createInt(), candidateName())

    fun ballot(): Ballot =
            Ballot(electionName(), voterName(), rankings(3), ballotAction())

    fun ballots(howMany: Int): List<Ballot> =
            ((1..howMany).map { ballot() })

    fun voterAndBallots(): VoterAndBallots =
            VoterAndBallots(voterName(), ballots(3))

    fun elections(): List<Election> = elections(3)
    fun elections(howMany: Int): List<Election> = (1..howMany).map { election() }
    fun election(): Election = Election(voterName(), electionName(), date(), date(), boolean(), electionStatus())
    fun candidates(): List<String> = candidates(3)
    fun candidates(howMany: Int): List<String> = ((1..howMany).map { candidateName() })
    fun date(): Date {
        index++
        return Date(Date.UTC(2019, 1, index))
    }

    fun boolean(): Boolean {
        index++
        return index % 2 == 0
    }

    fun electionStatus(): ElectionStatus {
        index++
        return enumValue(index)
    }

    fun createInt(): Int {
        index++
        return index
    }

    fun ballotAction(): Ballot.Action {
        index++
        return enumValue(index)
    }

    inline fun <reified T : Enum<T>> enumValue(index: Int): T {
        val enumValues = enumValues<T>()
        val moduloIndex = index % enumValues.size
        return enumValues[moduloIndex]
    }
}
