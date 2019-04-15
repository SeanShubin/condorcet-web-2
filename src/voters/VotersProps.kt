package voters

import model.ElectionAndVoters
import react.RProps

interface VotersProps : RProps {
    var electionAndVoters: ElectionAndVoters
}
