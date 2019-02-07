object Reactor {
    fun react(model:Model, event:EventInterface):Result {
        return when(event){
            is EventLogin ->{
                Result(model.copy(currentPage = "home"), listOf(EffectRender))
            }
            else -> {
                Result(model.copy(errorMessage = "unsupported event $event"), listOf(EffectRender))
            }
        }
    }
}
