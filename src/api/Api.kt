package api

import model.Ballot
import model.Credentials
import model.Election
import kotlin.js.Promise

interface Api {
    fun login(nameOrEmail: String, password: String): Promise<Credentials>
    fun register(name: String, email: String, password: String): Promise<Credentials>
    fun createElection(credentials: Credentials, electionName: String): Promise<Election>
    fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): Promise<Election>
    fun listElections(credentials: Credentials): Promise<List<Election>>
    fun getElection(credentials: Credentials, electionName: String): Promise<Election>
    fun doneEditingElection(credentials: Credentials, electionName: String): Promise<Election>
    fun startElection(credentials: Credentials, electionName: String): Promise<Election>
    fun endElection(credentials: Credentials, electionName: String): Promise<Election>
    fun listCandidates(credentials: Credentials, electionName: String): Promise<List<String>>
    fun updateCandidates(credentials: Credentials, electionName: String, candidates: List<String>): Promise<List<String>>
    fun listEligibleVoters(credentials: Credentials, electionName: String): Promise<List<String>>
    fun listAllVoters(credentials: Credentials): Promise<List<String>>
    fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoters: List<String>): Promise<Unit>
    fun listBallots(credentials: Credentials, voterName: String): Promise<List<Ballot>>
    fun getBallot(credentials: Credentials, electionName: String, voterName: String): Promise<Ballot>
    fun castBallot(credentials: Credentials, electionName: String, voterName: String, ballot: Ballot): Promise<Unit>
    fun setStartDate(credentials: Credentials, electionName: String, isoStartDate: String?): Promise<Election>
    fun setEndDate(credentials: Credentials, electionName: String, isoEndDate: String?): Promise<Election>
    fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): Promise<Election>
}
