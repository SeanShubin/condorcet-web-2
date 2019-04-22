package app

import api.Api
import ballot.ballot
import ballots.ballots
import candidates.candidates
import effect.Effect
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
import voters.voters

interface AppState : RState {
    var page: Page
}

interface AppProps : RProps {
    var eventLoop: EventLoop
    var environment: Environment
    var api: Api
}

class App : RComponent<AppProps, AppState>() {
    override fun AppState.init() {
        page = Page.initial
    }

    override fun RBuilder.render() {
        fun handleEvent(event: Event) {
            fun handleEffect(effect: Effect) {
                effect.apply(::handleEvent, props.environment)
            }
            try {
                val (newState, effects) = props.eventLoop.reactTo(state.page, event)
                setState {
                    page = newState
                    effects.forEach(::handleEffect)
                }
            } catch (ex: Throwable) {
                setState {
                    page = page.navError(ex.message ?: "<no message>")
                }
            }
        }
        when (val page = state.page) {
            is LoginPage -> login(::handleEvent, page.errorMessage)
            is RegisterPage -> register(::handleEvent, page.errorMessage)
            is HomePage -> home(::handleEvent)
            is ElectionsPage -> elections(page.elections)
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
