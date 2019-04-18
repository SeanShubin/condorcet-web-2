package prototype

import ballot.ballot
import ballots.ballots
import candidates.candidates
import election.election
import elections.elections
import event.Event
import home.home
import login.login
import logo.logo
import react.RBuilder
import react.dom.code
import react.dom.div
import react.dom.h2
import react.dom.p
import register.register
import sample.Sample
import ticker.ticker
import voters.voters

fun RBuilder.prototype(sendEvent: (Event) -> Unit) {
    val sample = Sample()
    div("App-header") {
        logo()
        h2 {
            +"Welcome to React with Kotlin"
        }
    }
    p("App-intro") {
        +"To get started, edit "
        code { +"app/App.kt" }
        +" and save to reload."
    }
    p("App-ticker") {
        ticker()
    }
    login(sendEvent)
    register(sendEvent)
    home(sendEvent)
    elections(sample.elections())
    election(sample.election())
    candidates(sample.electionAndCandidates())
    voters(sample.electionAndVoters())
    ballots(sample.voterAndBallots())
    ballot(sample.ballot())
}
