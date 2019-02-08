import kotlin.browser.window

object ModelRepositoryImpl:ModelRepository {
    override var model:Model get() {
        val state = window.localStorage.getItem("state")
        return Model.fromState(state)
    } set(model) {
        val state = model.toState()
        window.localStorage.setItem("state", state)
    }
}
