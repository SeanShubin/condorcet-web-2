package app

import api.Api
import kotlin.browser.window

class EnvironmentImpl(override val api: Api) : Environment {
    override fun setPathName(pathName: String) {
        console.log("setPathName($pathName)")
        window.location.pathname = pathName
    }
}
