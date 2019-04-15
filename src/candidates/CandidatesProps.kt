package candidates

import model.ElectionAndCandidates
import react.RProps

interface CandidatesProps : RProps {
    var electionAndCandidates: ElectionAndCandidates
}
