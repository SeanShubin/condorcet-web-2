package index

import api.Api
import api.ApiFake
import app.*
import clock.Clock
import clock.ClockImpl
import event.CondorcetEvent.ListElectionsRequest
import kotlinext.js.require
import kotlinext.js.requireAll
import model.Credentials
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    val aliceCredentials = Credentials("Alice", "password")
    val initialEvents = listOf(ListElectionsRequest(aliceCredentials))
    val eventLoop: EventLoop = EventLoopImpl()
    val api: Api = ApiFake()
    val clock: Clock = ClockImpl()
    val environment: Environment = EnvironmentImpl(api, clock)
    requireAll(require.context("src", true, js("/\\.css$/")))
    render(document.getElementById("root")) {
        app(eventLoop, environment, api, initialEvents)
    }
}
