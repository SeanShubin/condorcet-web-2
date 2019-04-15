package election

import model.Election
import react.RProps

interface ElectionProps : RProps {
    var election: Election
}
