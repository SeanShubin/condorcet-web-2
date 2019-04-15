package ballots

import model.VoterAndBallots
import react.RProps

interface BallotsProps : RProps {
    var voterAndBallots: VoterAndBallots
}
