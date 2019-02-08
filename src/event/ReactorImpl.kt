class ReactorImpl(private val renderableLookup: RenderableLookup) : Reactor {
    override fun react(model: Model, event: EventInterface): Result {
        return when (event) {
            is EventInitialize -> {
                Result(model.copy(currentPage = "login"), listOf(EffectRender(renderableLookup.byName("login"))))
            }
            is EventLogin -> {
                Result(model.copy(currentPage = "home"), listOf(EffectRender(renderableLookup.byName("home"))))
            }
            is NavigateTo -> {
                Result(model.copy(currentPage = event.pageName), listOf(EffectRender(renderableLookup.byName(event.pageName))))
            }
            else -> {
                Result(model.copy(errorMessage = "unsupported event $event"), listOf(EffectRender(renderableLookup.byName("debug"))))
            }
        }
    }
}
