package event

import model.Credentials

interface Event {
    object NavLoginRequest : Event
    data class LoginRequest(val nameOrEmail: String, val password: String) : Event
    data class LoginSuccess(val credentials: Credentials) : Event
    object LogoutRequest:Event
    object NavRegisterRequest : Event
    data class RegisterRequest(val name:String, val email:String, val password:String, val confirmPassword)
    object NavHomeRequest : Event
    object NavElectionsRequest : Event
    data class NavElectionRequest(val electionName: String) : Event
    data class NavCandidatesRequest(val electionName: String) : Event
    data class NavVotersRequest(val electionName: String) : Event
    data class NavBallotsRequest(val voterName: String) : Event
    data class NavBallotRequest(val electionName: String, val voterName: String) : Event
    object NavPrototypeRequest : Event
    data class Error(val message: String) : Event
}
