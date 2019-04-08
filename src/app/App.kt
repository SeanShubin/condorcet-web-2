package app

import ballot.ballot
import login.login
import react.*
import react.dom.*
import logo.*
import register.register
import sample.Sample
import ticker.*

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
    }
}

fun RBuilder.app() = child(App::class) {}
