package election

import kotlinx.html.InputType
import model.Election
import model.StringConversions.dateToString
import react.RBuilder
import react.dom.*

fun RBuilder.election(election: Election) {
    div(classes = "single-column-flex") {
        h1 { +"Election" }
        div(classes = "two-column-grid") {
            +"Election"
            input {
                attrs["value"] = election.name
            }
            +"Owner"
            input {
                attrs["value"] = election.owner
            }
            +"Status"
            input {
                attrs["value"] = election.status.description
            }
            +"Start"
            input {
                attrs["value"] = dateToString(election.start)
                attrs["placeholder"] = "YYYY-MM-DD HH:MM"
            }
            +"End"
            input {
                attrs["value"] = dateToString(election.end)
                attrs["placeholder"] = "YYYY-MM-DD HH:MM"
            }
        }
        span {
            input(type = InputType.checkBox) {}
            +"Secret Ballot"
        }
        a(href = "#") {
            +"Candidates (${election.candidateCount})"
        }
        a(href = "#") {
            +"Voters (${election.voterCount})"
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
            +"Home"
        }
        a(href = "#") {
            +"Logout"
        }
    }
}
