package app

import api.Api
import ballot.ballot
import ballots.ballots
import candidates.candidates
import election.election
import elections.elections
import error.errorPage
import event.Event
import home.home
import login.login
import pages.*
import prototype.prototype
import react.*
import register.register
import state.Model
import voters.voters

interface AppState : RState {
    var model: Model
}

interface AppProps : RProps {
    var eventLoop: EventLoop
    var environment: Environment
    var api: Api
}

class App : RComponent<AppProps, AppState>() {
    override fun AppState.init() {
        val page = Page.initial
        model = Model(page)
    }

    override fun RBuilder.render() {
        fun handleEvent(event: Event) {
            try {
                val (newState, effects) = props.eventLoop.reactTo(state.model, event)
                setState {
                    model = newState
                    effects.forEach { it.apply(::handleEvent, props.environment) }
                }
            } catch (ex: Throwable) {
                setState {
                    model = model.copy(page = model.page.navError(ex.message ?: "<no message>"))
                }
            }
        }
        when (val page = state.model.page) {
            is LoginPage -> login(::handleEvent, page.errorMessage)
            is RegisterPage -> register(::handleEvent, page.errorMessage)
            is HomePage -> home(::handleEvent, page.credentials)
            is ElectionsPage -> elections(::handleEvent, page.credentials, page.elections)
            is ElectionPage -> election(page.election)
            is CandidatesPage -> candidates(page.electionName, page.candidates)
            is VotersPage -> voters(page.electionName, page.voters)
            is BallotsPage -> ballots(page.voterName, page.ballots)
            is BallotPage -> ballot(page.ballot)
            is PrototypePage -> prototype(::handleEvent)
            is UnexpectedErrorPage -> errorPage(::handleEvent, page.message)
            else -> prototype(::handleEvent)
        }
    }
}

fun RBuilder.app(eventLoop: EventLoop, environment: Environment, api: Api) = child(App::class) {
    attrs.eventLoop = eventLoop
    attrs.environment = environment
    attrs.api = api
}
