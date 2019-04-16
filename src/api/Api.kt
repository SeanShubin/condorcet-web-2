package api

import model.Ballot
import model.Election
import kotlin.js.Promise

interface Api {
    fun login(nameOrEmail: String, password: String): Promise<Response<Unit>>
    fun logout(): Promise<Response<Unit>>
    fun register(name: String, email: String, password: String): Promise<Response<Unit>>
    fun createElection(electionName: String): Promise<Response<Unit>>
    fun copyElection(newElectionName: String, electionToCopyName: String): Promise<Response<Unit>>
    fun listElections(): Promise<Response<List<Election>>>
    fun getElection(electionName: String): Promise<Response<Election>>
    fun doneEditingElection(electionName: String): Promise<Response<Unit>>
    fun startElection(electionName: String): Promise<Response<Unit>>
    fun endElection(electionName: String): Promise<Response<Unit>>
    fun listCandidates(electionName: String): Promise<Response<List<String>>>
    fun updateCandidates(electionName: String, candidates: List<String>): Promise<Response<Unit>>
    fun listEligibleVoters(electionName: String): Promise<Response<List<String>>>
    fun listAllVoters(): Promise<Response<List<String>>>
    fun updateEligibleVoters(electionName: String, eligibleVoters: List<String>): Promise<Response<Unit>>
    fun listBallots(voterName: String): Promise<Response<List<Ballot>>>
    fun getBallot(electionName: String, voterName: String): Promise<Response<Ballot>>
    fun castBallot(electionName: String, voterName: String, ballot: Ballot): Promise<Response<Unit>>

    interface Response<T>
    data class Success<T>(val value: T) : Response<T>
    data class Failure<T>(val message: String) : Response<T>
}
