package sample

import ballot.BallotProps
import ballot.RankingProps
import elections.ElectionsProps
import model.Election
import model.Election.ElectionStatus
import kotlin.js.Date

class Sample {
    private var index = 0
    fun electionName():String {
        index++
        return "Election $index"
    }

    fun voterName():String{
        index++
        return "Voter $index"
    }

    fun candidateName():String{
        index++
        return "Candidate $index"
    }

    fun rank():Int {
        index++
        return index
    }

    fun ranking():RankingProps = object:RankingProps{
            override val rank: Int = rank()
            override val candidateName: String = candidateName()
    }

    fun rankings(howMany:Int):List<RankingProps> = (1..howMany).map{ranking()}

    fun ballot():BallotProps =object:BallotProps{
        override var electionName: String = electionName()
        override var voterName: String = voterName()
        override var rankings: List<RankingProps> = rankings(3)
    }

    fun elections(): ElectionsProps = elections(3)
    fun elections(howMany: Int): ElectionsProps = object : ElectionsProps {
        override var elections: List<Election> = ((1..howMany).map { election() })
    }

    fun election(): Election = Election(voterName(), electionName(), date(), date(), boolean(), electionStatus())
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

    inline fun <reified T : Enum<T>> enumValue(index: Int): T {
        val enumValues = enumValues<T>()
        val moduloIndex = index % enumValues.size
        return enumValues[moduloIndex]
    }
}
