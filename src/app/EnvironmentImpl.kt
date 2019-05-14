package app

import api.Api
import clock.Clock

class EnvironmentImpl(override val api: Api,
                      override val clock: Clock) : Environment

