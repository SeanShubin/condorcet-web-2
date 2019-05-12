package event

import model.Ballot
import model.Credentials
import model.Election

interface Event {
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

    data class RegisterRequest(
            val name: String,
            val email: String,
            val password: String,
            val confirmPassword: String) : Event

    data class NavHomeRequest(val credentials: Credentials) : Event
    data class RegisterFailure(val message: String) : Event
    data class NavElectionRequest(val electionName: String) : Event
    data class NavCandidatesRequest(val electionName: String) : Event
    data class NavVotersRequest(val electionName: String) : Event
    data class NavBallotsRequest(val credentials: Credentials) : Event
    data class NavBallotRequest(val electionName: String, val voterName: String) : Event
    object NavPrototypeRequest : Event {
        override fun toString(): String = "NavPrototypeRequest"
    }

    data class Error(val message: String) : Event
    data class ListElectionsRequest(val credentials: Credentials) : Event
    data class ListElectionsSuccess(val credentials: Credentials, val elections: List<Election>) : Event
    data class ListElectionsFailure(val message: String) : Event
    data class CreateElectionRequest(val credentials: Credentials, val electionName: String) : Event
    data class CreateElectionSuccess(val credentials: Credentials, val election: Election) : Event
    data class CreateElectionFailure(val message: String) : Event
    data class CopyElectionRequest(val credentials: Credentials, val electionName: String) : Event
    data class EditElectionRequest(val credentials: Credentials, val electionName: String) : Event
    data class ListBallotsRequest(val credentials: Credentials) : Event
    data class ListBallotsSuccess(val credentials: Credentials,
                                  val voterName: String,
                                  val ballots: List<Ballot>) : Event

    data class ListBallotsFailure(val message: String) : Event
}
