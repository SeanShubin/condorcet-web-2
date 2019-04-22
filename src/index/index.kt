package index

import api.Api
import api.ApiFake
import app.*
import kotlinext.js.require
import kotlinext.js.requireAll
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    val eventLoop: EventLoop = EventLoopImpl()
    val api: Api = ApiFake()
    val environment: Environment = EnvironmentImpl(api)
    requireAll(require.context("src", true, js("/\\.css$/")))
    render(document.getElementById("root")) {
        app(eventLoop, environment, api)
    }
}
