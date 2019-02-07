data class Model(val currentPage:String, val errorMessage:String?){
    fun toState():String = JSON.stringify(this)
    companion object {
        fun fromState(state:String?):Model = if(state == null) Model("login", "") else JSON.parse(state)
    }
}
