package ballot

import react.RProps

interface RankingProps: RProps {
    val rank:Int
    val candidateName:String
}
