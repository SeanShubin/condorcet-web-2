package ballots

import model.Ballot
import react.RProps

interface BallotsProps : RProps {
    var voterName: String
    var ballots: List<Ballot>
}
