package sample

import ballot.BallotProps
import ballot.RankingProps

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
}
