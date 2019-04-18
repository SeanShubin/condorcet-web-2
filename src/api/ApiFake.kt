package api

import model.Ballot
import model.Election
import model.User
import kotlin.js.Promise

class ApiFake : Api {
    private val users: MutableList<User> = mutableListOf()
    override fun login(nameOrEmail: String, password: String): Promise<Api.Response<Unit>> {
        val user = findUser(nameOrEmail)
        return when {
            user == null -> loginError(nameOrEmail)
            user.password == password -> Promise.resolve(Api.Success<Unit>(Unit))
            else -> loginError(nameOrEmail)
        }
    }

    override fun logout(): Promise<Api.Response<Unit>> {
        TODO("not implemented")
    }

    override fun register(name: String, email: String, password: String): Promise<Api.Response<Unit>> {
        TODO("not implemented")
    }

    override fun createElection(electionName: String): Promise<Api.Response<Unit>> {
        TODO("not implemented")
    }

    override fun copyElection(newElectionName: String, electionToCopyName: String): Promise<Api.Response<Unit>> {
        TODO("not implemented")
    }

    override fun listElections(): Promise<Api.Response<List<Election>>> {
        TODO("not implemented")
    }

    override fun getElection(electionName: String): Promise<Api.Response<Election>> {
        TODO("not implemented")
    }

    override fun doneEditingElection(electionName: String): Promise<Api.Response<Unit>> {
        TODO("not implemented")
    }

    override fun startElection(electionName: String): Promise<Api.Response<Unit>> {
        TODO("not implemented")
    }

    override fun endElection(electionName: String): Promise<Api.Response<Unit>> {
        TODO("not implemented")
    }

    override fun listCandidates(electionName: String): Promise<Api.Response<List<String>>> {
        TODO("not implemented")
    }

    override fun updateCandidates(electionName: String, candidates: List<String>): Promise<Api.Response<Unit>> {
        TODO("not implemented")
    }

    override fun listEligibleVoters(electionName: String): Promise<Api.Response<List<String>>> {
        TODO("not implemented")
    }

    override fun listAllVoters(): Promise<Api.Response<List<String>>> {
        TODO("not implemented")
    }

    override fun updateEligibleVoters(electionName: String, eligibleVoters: List<String>): Promise<Api.Response<Unit>> {
        TODO("not implemented")
    }

    override fun listBallots(voterName: String): Promise<Api.Response<List<Ballot>>> {
        TODO("not implemented")
    }

    override fun getBallot(electionName: String, voterName: String): Promise<Api.Response<Ballot>> {
        TODO("not implemented")
    }

    override fun castBallot(electionName: String, voterName: String, ballot: Ballot): Promise<Api.Response<Unit>> {
        TODO("not implemented")
    }

    private fun findUser(nameOrEmail: String): User? = TODO()

    private fun loginError(nameOrEmail: String): Promise<Api.Response<Unit>> =
            Promise.resolve(Api.Failure("Unable to authenticate user '$nameOrEmail'"))
}
