package election

import kotlinx.html.InputType
import model.Election
import model.StringConversions.dateToString
import react.RBuilder
import react.RComponent
import react.RState
import react.dom.*

class ElectionComponent : RComponent<ElectionProps, RState>() {
    override fun RBuilder.render() {
        div(classes = "single-column-flex") {
            h1 { +"Election" }
            div(classes = "two-column-grid") {
                +"Election"
                input {
                    attrs["value"] = props.election.name
                }
                +"Owner"
                input {
                    attrs["value"] = props.election.owner
                }
                +"Status"
                input {
                    attrs["value"] = props.election.status.description
                }
                +"Start"
                input {
                    attrs["value"] = dateToString(props.election.start)
                    attrs["placeholder"] = "YYYY-MM-DD HH:MM"
                }
                +"End"
                input {
                    attrs["value"] = dateToString(props.election.end)
                    attrs["placeholder"] = "YYYY-MM-DD HH:MM"
                }
            }
            span {
                input(type = InputType.checkBox) {}
                +"Secret Ballot"
            }
            button {
                +"Done Editing"
            }
            button {
                +"Start Now"
            }
            button {
                +"End Now"
            }
            a(href = "#") {
                +"Candidates"
            }
            a(href = "#") {
                +"Voters"
            }
            a(href = "#") {
                +"Home"
            }
            a(href = "#") {
                +"Logout"
            }
        }
    }
}

fun RBuilder.election(election: Election) = child(ElectionComponent::class) {
    this.attrs.election = election
}
