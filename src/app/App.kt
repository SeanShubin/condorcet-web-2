package app

import ballot.ballot
import ballots.ballots
import candidates.candidates
import election.election
import elections.elections
import home.home
import kotlinx.html.js.onClickFunction
import login.login
import prototype.prototype
import react.RBuilder
import react.RComponent
import react.RProps
import react.dom.a
import react.dom.div
import react.dom.p
import react.setState
import register.register
import sample.Sample
import voters.voters

class App : RComponent<RProps, AppState>() {
    override fun AppState.init() {
        pageName = "login"
    }
    override fun RBuilder.render() {
        val sample = Sample()
        when {
            state.pageName == "register" -> register()
            state.pageName == "home" -> home()
            state.pageName == "elections" -> elections(sample.elections())
            state.pageName == "election" -> election(sample.election())
            state.pageName == "candidates" -> candidates(sample.electionAndCandidates())
            state.pageName == "voters" -> voters(sample.electionAndVoters())
            state.pageName == "ballots" -> ballots(sample.voterAndBallots())
            state.pageName == "ballot" -> ballot(sample.ballot())
            state.pageName == "prototype" -> prototype()
            else -> login()
        }
        p {
            +JSON.stringify(state)
        }
        div {
            a(href = "#") {
                +"Prototype"
                attrs.onClickFunction = {
                    setState {
                        pageName = "prototype"
                    }
                }
            }
        }
    }
}

fun RBuilder.app() = child(App::class) {}
