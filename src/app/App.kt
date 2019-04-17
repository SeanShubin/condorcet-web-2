package app

import ballot.ballot
import ballots.ballots
import candidates.candidates
import election.election
import elections.elections
import home.home
import login.login
import prototype.prototype
import react.RBuilder
import react.RComponent
import react.RProps
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
        val navigateTo: (String) -> Unit = { name ->
            setState {
                pageName = name
            }
        }
        when {
            state.pageName == "login" -> login(navigateTo)
            state.pageName == "register" -> register(navigateTo)
            state.pageName == "home" -> home(navigateTo)
            state.pageName == "elections" -> elections(sample.elections())
            state.pageName == "election" -> election(sample.election())
            state.pageName == "candidates" -> candidates(sample.electionAndCandidates())
            state.pageName == "voters" -> voters(sample.electionAndVoters())
            state.pageName == "ballots" -> ballots(sample.voterAndBallots())
            state.pageName == "ballot" -> ballot(sample.ballot())
            state.pageName == "prototype" -> prototype(navigateTo)
            else -> prototype(navigateTo)
        }
    }
}

fun RBuilder.app() = child(App::class) {}
