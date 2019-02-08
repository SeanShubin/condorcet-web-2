class ReactorImpl : Reactor {
    override fun react(model: Model, event: EventInterface): Result {
        console.log("ReactorImpl::model", model)
        console.log("ReactorImpl::event", event)
        val result = when (event) {
            is EventInitialize -> {
                Result(Model.default, listOf(EffectRender))
            }
            is EventLogin -> {
                Result(model.copy(currentPage = "home"), listOf(EffectRender))
            }
            is NavigateTo -> {
                Result(model.copy(currentPage = event.pageName), listOf(EffectRender))
            }
            else -> {
                val errorMessage = "unsupported event $event"
                Result(model.copy(currentPage = "debug", errorMessage = errorMessage), listOf(EffectRender))
            }
        }
        console.log("ReactorImpl::result", result)
        return result
    }
}
