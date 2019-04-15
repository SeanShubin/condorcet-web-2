package app

import ballot.ballot
import ballots.ballots
import candidates.candidates
import election.election
import elections.elections
import home.home
import login.login
import logo.logo
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.code
import react.dom.div
import react.dom.h2
import react.dom.p
import register.register
import sample.Sample
import ticker.ticker
import voters.voters

class App : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
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
        login()
        register()
        home()
        elections(sample.elections())
        election(sample.election())
        candidates(sample.electionAndCandidates())
        voters(sample.electionAndVoters())
        ballot(sample.ballot())
        ballots(sample.voterAndBallots())
    }
}

fun RBuilder.app() = child(App::class) {}
