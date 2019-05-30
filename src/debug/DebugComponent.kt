package debug

import event.CondorcetEvent
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.dom.a
import react.dom.div
import react.dom.p
import state.Model

fun RBuilder.debug(sendEvent: (CondorcetEvent) -> Unit, model: Model) {
    div(classes = "single-column-flex") {
        p {
            +JSON.stringify(model)
        }
        a(href = "#") {
            +"Home"
            attrs {
                onClickFunction = {
                    sendEvent(CondorcetEvent.NavLoginRequest)
                }
            }
        }
    }
}
