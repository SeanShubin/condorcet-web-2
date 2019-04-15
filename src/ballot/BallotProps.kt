package ballot

import react.RProps

interface BallotProps : RProps {
    var electionName: String
    var voterName: String
    var rankings: List<RankingProps>
}