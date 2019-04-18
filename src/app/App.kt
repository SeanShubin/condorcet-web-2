package app

import api.Api
import ballot.ballot
import ballots.ballots
import candidates.candidates
import effect.Effect
import election.election
import elections.elections
import event.Event
import home.home
import login.login
import model.State
import prototype.prototype
import react.*
import register.register
import voters.voters

interface AppState : RState {
    var model: State
}

interface AppProps : RProps {
    var eventLoop: EventLoop
    var environment: Environment
    var api: Api
}

class App : RComponent<AppProps, AppState>() {
    override fun AppState.init() {
        model = State.initial
    }

    override fun RBuilder.render() {
        fun handleEffect(effect: Effect) {
            props.environment.handleEffect(state.model, props.eventLoop, props.api, effect)
        }

        fun handleEvent(event: Event) {
            console.log("App::handleEvent($event)")
            setState {
                val (newState, effects) = props.eventLoop.reactTo(state.model, event)
                console.log("newState = $newState")
                console.log("effects = $effects")
                model = newState
                effects.forEach(::handleEffect)
            }
        }
        when {
            state.model.pageName == "login" -> login(::handleEvent)
            state.model.pageName == "register" -> register(::handleEvent)
            state.model.pageName == "home" -> home(::handleEvent)
            state.model.pageName == "elections" -> elections(state.model.electionsPage!!)
            state.model.pageName == "election" -> election(state.model.electionPage!!)
            state.model.pageName == "candidates" -> candidates(state.model.candidatesPage!!)
            state.model.pageName == "voters" -> voters(state.model.votersPage!!)
            state.model.pageName == "ballots" -> ballots(state.model.ballotsPage!!)
            state.model.pageName == "ballot" -> ballot(state.model.ballotPage!!)
            state.model.pageName == "error" -> error(state.model.errorPage!!)
            state.model.pageName == "prototype" -> prototype(::handleEvent)
            else -> prototype(::handleEvent)
        }
    }
}

fun RBuilder.app(eventLoop: EventLoop, environment: Environment, api: Api) = child(App::class) {
    attrs.eventLoop = eventLoop
    attrs.environment = environment
    attrs.api = api
}
