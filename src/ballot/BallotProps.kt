package ballot

import model.Ballot
import react.RProps

interface BallotProps : RProps {
    var ballot: Ballot
}
