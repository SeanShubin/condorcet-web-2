package app

import ballot.ballot
import elections.elections
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
        ballot(sample.ballot())
        elections(sample.elections())
    }
}

fun RBuilder.app() = child(App::class) {}
