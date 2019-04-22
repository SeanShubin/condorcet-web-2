package app

import api.Api

interface Environment {
    val api: Api
    fun setPathName(pathName: String)
}
