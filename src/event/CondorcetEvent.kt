package event

import model.Ballot
import model.Credentials
import model.Election
import model.Ranking

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
    data class ListElectionsSuccess(val credentials: Credentials,
                                    val elections: List<Election>) : CondorcetEvent

    data class ListElectionsFailure(val message: String) : CondorcetEvent
    data class CreateElectionRequest(val credentials: Credentials,
                                     val electionName: String) : CondorcetEvent

    data class CreateElectionSuccess(val credentials: Credentials,
                                     val election: Election) : CondorcetEvent

    data class CreateElectionFailure(val message: String) : CondorcetEvent
    data class CopyElectionRequest(val credentials: Credentials,
                                   val newElectionName: String,
                                   val electionToCopyName: String) : CondorcetEvent

    data class LoadElectionRequest(val credentials: Credentials,
                                   val electionName: String) : CondorcetEvent

    data class LoadElectionSuccess(val credentials: Credentials,
                                   val election: Election) : CondorcetEvent

    data class LoadElectionFailure(val message: String) : CondorcetEvent
    data class ListBallotsRequest(val credentials: Credentials) : CondorcetEvent
    data class ListBallotsSuccess(val credentials: Credentials,
                                  val voterName: String,
                                  val ballots: List<Ballot>) : CondorcetEvent

    data class ListBallotsFailure(val message: String) : CondorcetEvent
    data class ListCandidatesRequest(val credentials: Credentials,
                                     val electionName: String) : CondorcetEvent

    data class ListCandidatesSuccess(val credentials: Credentials,
                                     val electionName: String,
                                     val candidates: List<String>) : CondorcetEvent

    data class ListCandidatesFailure(val message: String) : CondorcetEvent
    data class ListVotersRequest(val credentials: Credentials,
                                 val electionName: String) : CondorcetEvent

    data class ListVotersSuccess(val credentials: Credentials,
                                 val electionName: String,
                                 val voters: List<String>,
                                 val isAllVoters: Boolean) : CondorcetEvent

    data class UpdateCandidatesRequest(
            val credentials: Credentials,
            val electionName: String,
            val candidates: List<String>) : CondorcetEvent

    data class UpdateVotersRequest(
            val credentials: Credentials,
            val electionName: String,
            val voters: List<String>) : CondorcetEvent

    data class UpdateToAllVotersRequest(
            val credentials: Credentials,
            val electionName: String) : CondorcetEvent

    data class ListVotersFailure(val message: String) : CondorcetEvent
    data class DoneEditingRequest(val credentials: Credentials,
                                  val electionName: String) : CondorcetEvent

    data class EndNowRequest(val credentials: Credentials,
                             val electionName: String) : CondorcetEvent

    data class UpdateElectionEndDateRequest(val credentials: Credentials,
                                            val electionName: String,
                                            val endDate: String) : CondorcetEvent

    data class UpdateElectionSecretBallotRequest(val credentials: Credentials,
                                                 val electionName: String,
                                                 val checked: Boolean) : CondorcetEvent

    data class StartDateChanged(val startDate: String) : CondorcetEvent
    data class EndDateChanged(val endDate: String) : CondorcetEvent
    data class LoadBallotRequest(val credentials: Credentials,
                                 val electionName: String,
                                 val voterName: String) : CondorcetEvent

    data class LoadBallotSuccess(val credentials: Credentials,
                                 val ballot: Ballot) : CondorcetEvent

    data class LoadBallotFailure(val message: String) : CondorcetEvent
    data class RankChanged(val index: Int, val rank: String) : CondorcetEvent
    data class CastBallotRequest(val credentials: Credentials,
                                 val electionName: String,
                                 val voterName: String,
                                 val rankings: List<Ranking>) : CondorcetEvent
}
