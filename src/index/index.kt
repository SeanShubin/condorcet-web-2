package index

import app.EventLoop
import app.EventLoopImpl
import app.app
import kotlinext.js.require
import kotlinext.js.requireAll
import react.dom.render
import kotlin.browser.document

fun main(args: Array<String>) {
    val eventLoop: EventLoop = EventLoopImpl()
    requireAll(require.context("src", true, js("/\\.css$/")))
    render(document.getElementById("root")) {
        app(eventLoop)
    }
}
