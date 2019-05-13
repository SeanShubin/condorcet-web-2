package event

import model.Ballot
import model.Credentials
import model.Election

interface CondorcetEvent {
    object NavLoginRequest : CondorcetEvent {
        override fun toString(): String = "NavLoginRequest"
    }

    data class LoginRequest(val nameOrEmail: String, val password: String) : CondorcetEvent
    data class LoginSuccess(val credentials: Credentials) : CondorcetEvent
    data class LoginFailure(val message: String) : CondorcetEvent
    object LogoutRequest : CondorcetEvent {
        override fun toString(): String = "LogoutRequest"
    }

    object NavRegisterRequest : CondorcetEvent {
        override fun toString(): String = "NavRegisterRequest"
    }

    data class RegisterRequest(
            val name: String,
            val email: String,
            val password: String,
            val confirmPassword: String) : CondorcetEvent

    data class NavHomeRequest(val credentials: Credentials) : CondorcetEvent
    data class RegisterFailure(val message: String) : CondorcetEvent
    object NavPrototypeRequest : CondorcetEvent {
        override fun toString(): String = "NavPrototypeRequest"
    }

    data class Error(val message: String) : CondorcetEvent
    data class ListElectionsRequest(val credentials: Credentials) : CondorcetEvent
    data class ListElectionsSuccess(val credentials: Credentials, val elections: List<Election>) : CondorcetEvent
    data class ListElectionsFailure(val message: String) : CondorcetEvent
    data class CreateElectionRequest(val credentials: Credentials, val electionName: String) : CondorcetEvent
    data class CreateElectionSuccess(val credentials: Credentials, val election: Election) : CondorcetEvent
    data class CreateElectionFailure(val message: String) : CondorcetEvent
    data class CopyElectionRequest(val credentials: Credentials, val electionName: String) : CondorcetEvent
    data class LoadElectionRequest(val credentials: Credentials, val electionName: String) : CondorcetEvent
    data class LoadElectionSuccess(val credentials: Credentials, val election: Election) : CondorcetEvent
    data class LoadElectionFailure(val message: String) : CondorcetEvent
    data class ListBallotsRequest(val credentials: Credentials) : CondorcetEvent
    data class ListBallotsSuccess(val credentials: Credentials,
                                  val voterName: String,
                                  val ballots: List<Ballot>) : CondorcetEvent

    data class ListBallotsFailure(val message: String) : CondorcetEvent
    data class ListCandidatesRequest(val credentials: Credentials, val electionName: String) : CondorcetEvent
    data class ListVotersRequest(val credentials: Credentials, val electionName: String) : CondorcetEvent
    data class DoneEditingRequest(val credentials: Credentials, val electionName: String) : CondorcetEvent
    data class StartNowRequest(val credentials: Credentials, val electionName: String) : CondorcetEvent
    data class EndNowRequest(val credentials: Credentials, val electionName: String) : CondorcetEvent
    data class UpdateStartDate(val credentials: Credentials, val electionName: String, val startDate: String) : CondorcetEvent
    data class UpdateEndDate(val credentials: Credentials, val electionName: String, val endDate: String) : CondorcetEvent
    data class UpdateSecretBallot(val credentials: Credentials, val electionName: String, val checked: Boolean) : CondorcetEvent
}
