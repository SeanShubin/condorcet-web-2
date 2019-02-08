class RenderableLookupImpl:RenderableLookup{
    private val loginPage = Login()
    private val registerPage = Register()
    private val debugPage = Debug()
    private val homePage = Home()
    private val pages: Map<String, Renderable> = mapOf(
            Pair("login", loginPage),
            Pair("register", registerPage),
            Pair("home", homePage),
            Pair("debug", debugPage))

    override fun byName(name:String):Renderable {
        return pages[name] ?: debugPage
    }
}
