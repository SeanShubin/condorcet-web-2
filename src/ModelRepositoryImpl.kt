class ModelRepositoryImpl(private val environment: Environment) : ModelRepository {
    override var model: Model
        get() {
            val state = environment.window.localStorage.getItem("state")
            return Model.fromState(state)
        }
        set(model) {
            val state = model.toState()
            environment.window.localStorage.setItem("state", state)
        }
}
