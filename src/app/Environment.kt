package app

import api.Api
import clock.Clock

interface Environment {
    val api: Api
    val clock: Clock
}
