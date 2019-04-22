package event

import model.Credentials

interface Event {
    class PathNameChanged(val pathName: String) : Event
    object NavLoginRequest : Event {
        override fun toString(): String = "NavLoginRequest"
    }
    data class LoginRequest(val nameOrEmail: String, val password: String) : Event
    data class LoginSuccess(val credentials: Credentials) : Event
    data class LoginFailure(val message: String) : Event
    object LogoutRequest : Event {
        override fun toString(): String = "LogoutRequest"
    }

    object NavRegisterRequest : Event {
        override fun toString(): String = "NavRegisterRequest"
    }
    data class RegisterRequest(val name: String, val email: String, val password: String, val confirmPassword: String) : Event
    object NavHomeRequest : Event {
        override fun toString(): String = "NavHomeRequest"
    }

    data class RegisterFailure(val message: String) : Event

    object NavElectionsRequest : Event {
        override fun toString(): String = "NavElectionsRequest"
    }
    data class NavElectionRequest(val electionName: String) : Event
    data class NavCandidatesRequest(val electionName: String) : Event
    data class NavVotersRequest(val electionName: String) : Event
    data class NavBallotsRequest(val voterName: String) : Event
    data class NavBallotRequest(val electionName: String, val voterName: String) : Event
    object NavPrototypeRequest : Event {
        override fun toString(): String = "NavPrototypeRequest"
    }
    data class Error(val message: String) : Event
}
