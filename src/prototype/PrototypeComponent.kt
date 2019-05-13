package prototype

import ballot.ballot
import ballots.ballots
import candidates.candidates
import election.election
import elections.elections
import event.CondorcetEvent
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

fun RBuilder.prototype(sendEvent: (CondorcetEvent) -> Unit) {
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
    login(sendEvent, errorMessage = null)
    register(sendEvent, errorMessage = null)
    home(sendEvent, sample.credentials())
    elections(sendEvent, sample.credentials(), sample.elections())
    election(sendEvent, sample.credentials(), sample.election())
    candidates(sendEvent, sample.credentials(), sample.electionName(), sample.candidates())
    voters(sendEvent, sample.credentials(), sample.electionName(), sample.voters())
    ballots(sendEvent, sample.credentials(), sample.voterName(), sample.ballots())
    ballot(sendEvent, sample.credentials(), sample.ballot())
}
