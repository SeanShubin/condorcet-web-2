class ReactorImpl : Reactor {
    override fun react(model: Model, event: EventInterface): Result {
        return when (event) {
            is EventInitialize -> {
                Result(model.copy(currentPage = "login"), listOf(EffectRender))
            }
            is EventLogin -> {
                Result(model.copy(currentPage = "home"), listOf(EffectRender))
            }
            is NavigateTo -> {
                Result(model.copy(currentPage = event.pageName), listOf(EffectRender))
            }
            else -> {
                Result(model.copy(currentPage = "debug", errorMessage = "unsupported event $event"), listOf(EffectRender))
            }
        }
    }
}
