package index

import api.Api
import api.ApiFake
import app.*
import clock.Clock
import clock.ClockImpl
import db.Db
import db.InMemoryDb
import event.CondorcetEvent
import kotlinext.js.require
import kotlinext.js.requireAll
import model.Credentials
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    val aliceCredentials = Credentials("Alice", "password")
    val initialEvents = listOf(CondorcetEvent.ListBallotsRequest(aliceCredentials))
    val eventLoop: EventLoop = EventLoopImpl()
    val clock: Clock = ClockImpl()
    val db: Db = InMemoryDb()
    val api: Api = ApiFake(clock, db)
    val environment: Environment = EnvironmentImpl(api, clock)
    requireAll(require.context("src", true, js("/\\.css$/")))
    render(document.getElementById("root")) {
        app(eventLoop, environment, api, initialEvents)
    }
}
