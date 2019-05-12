package index

import api.Api
import api.ApiFake
import app.*
import event.Event
import kotlinext.js.require
import kotlinext.js.requireAll
import model.Credentials
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    val aliceCredentials = Credentials("Alice", "password")
    val initialEvents = listOf(
            Event.RegisterRequest("Alice", "alice@email.com", "password", "password"),
            Event.RegisterRequest("Bob", "bob@email.com", "password", "password"),
            Event.RegisterRequest("Carol", "carol@email.com", "password", "password"),
            Event.RegisterRequest("Dave", "dave@email.com", "password", "password"),
            Event.CreateElectionRequest(aliceCredentials, "Election 1"),
            Event.NavHomeRequest(aliceCredentials))
    val eventLoop: EventLoop = EventLoopImpl()
    val api: Api = ApiFake()
    val environment: Environment = EnvironmentImpl(api)
    requireAll(require.context("src", true, js("/\\.css$/")))
    render(document.getElementById("root")) {
        app(eventLoop, environment, api, initialEvents)
    }
}
