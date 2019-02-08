data class Model(val currentPage:String, val errorMessage:String?){
    fun toState():String = JSON.stringify(this)
    companion object {
        val default = Model(
                currentPage = "login",
                errorMessage = null
        )
        fun fromState(state:String?):Model =
                if(state == null) default
                else {
                    val dynamicModel:Model = JSON.parse(state)
                    val model = Model(dynamicModel.currentPage, dynamicModel.errorMessage)
                    model
                }
    }
}
