package app

import api.Api
import ballot.ballot
import ballots.ballots
import candidates.candidates
import election.election
import elections.elections
import error.errorPage
import event.CondorcetEvent
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
    var initialEvents: List<CondorcetEvent>
}

class App : RComponent<AppProps, AppState>() {
    override fun AppState.init() {
        val page = Page.initial
        model = Model(page)
    }

    override fun componentDidMount() {
        props.initialEvents.forEach(::handleEvent)
    }

    private fun handleEvent(event: CondorcetEvent) {
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

    override fun RBuilder.render() {
        when (val page = state.model.page) {
            is LoginPage -> login(::handleEvent, page.errorMessage)
            is RegisterPage -> register(::handleEvent, page.errorMessage)
            is HomePage -> home(::handleEvent, page.credentials)
            is ElectionsPage -> elections(::handleEvent, page.credentials, page.elections)
            is ElectionPage -> election(::handleEvent, page.credentials, page.election)
            is CandidatesPage -> candidates(::handleEvent, page.credentials, page.electionName, page.candidates)
            is VotersPage -> voters(::handleEvent, page.credentials, page.electionName, page.voters)
            is BallotsPage -> ballots(::handleEvent, page.credentials, page.voterName, page.ballots)
            is BallotPage -> ballot(::handleEvent, page.credentials, page.ballot)
            is PrototypePage -> prototype(::handleEvent)
            is UnexpectedErrorPage -> errorPage(::handleEvent, page.message)
            else -> prototype(::handleEvent)
        }
    }
}

fun RBuilder.app(eventLoop: EventLoop,
                 environment: Environment,
                 api: Api,
                 initialEvents: List<CondorcetEvent>) = child(App::class) {
    attrs.eventLoop = eventLoop
    attrs.environment = environment
    attrs.api = api
    attrs.initialEvents = initialEvents
}
