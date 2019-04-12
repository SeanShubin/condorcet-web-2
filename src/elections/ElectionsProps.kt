package elections

import model.Election
import react.RProps

interface ElectionsProps : RProps {
    var elections: List<Election>
}
