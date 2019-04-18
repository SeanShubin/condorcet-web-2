package api

import model.Ballot
import model.Credentials
import model.Election
import model.User
import kotlin.js.Promise

class ApiFake : Api {
    private val users: MutableList<User> = mutableListOf()
    override fun login(nameOrEmail: String, password: String): Promise<Credentials> {
        val user = findUser(nameOrEmail)
        return when {
            user == null -> loginError(nameOrEmail)
            user.password == password -> Promise.resolve(Credentials(user.name, user.password))
            else -> loginError(nameOrEmail)
        }
    }

    override fun logout(): Promise<Unit> = Promise.resolve(Unit)

    override fun register(name: String, email: String, password: String): Promise<Credentials> {
        return try {
            assertUserNameDoesNotExist(name)
            assertUserEmailDoesNotExist(email)
            val user = createUser(name, email, password)
            Promise.resolve(Credentials(user.name, user.password))
        } catch (ex: RuntimeException) {
            Promise.reject(ex)
        }
    }

    override fun createElection(credentials: Credentials, electionName: String): Promise<Unit> {
        TODO("not implemented")
    }

    override fun copyElection(credentials: Credentials, newElectionName: String, electionToCopyName: String): Promise<Unit> {
        TODO("not implemented")
    }

    override fun listElections(credentials: Credentials): Promise<List<Election>> {
        TODO("not implemented")
    }

    override fun getElection(credentials: Credentials, electionName: String): Promise<Election> {
        TODO("not implemented")
    }

    override fun doneEditingElection(credentials: Credentials, electionName: String): Promise<Unit> {
        TODO("not implemented")
    }

    override fun startElection(credentials: Credentials, electionName: String): Promise<Unit> {
        TODO("not implemented")
    }

    override fun endElection(credentials: Credentials, electionName: String): Promise<Unit> {
        TODO("not implemented")
    }

    override fun listCandidates(credentials: Credentials, electionName: String): Promise<List<String>> {
        TODO("not implemented")
    }

    override fun updateCandidates(credentials: Credentials, electionName: String, candidates: List<String>): Promise<Unit> {
        TODO("not implemented")
    }

    override fun listEligibleVoters(credentials: Credentials, electionName: String): Promise<List<String>> {
        TODO("not implemented")
    }

    override fun listAllVoters(credentials: Credentials): Promise<List<String>> {
        TODO("not implemented")
    }

    override fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoters: List<String>): Promise<Unit> {
        TODO("not implemented")
    }

    override fun listBallots(credentials: Credentials, voterName: String): Promise<List<Ballot>> {
        TODO("not implemented")
    }

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Promise<Ballot> {
        TODO("not implemented")
    }

    override fun castBallot(credentials: Credentials, electionName: String, voterName: String, ballot: Ballot): Promise<Unit> {
        TODO("not implemented")
    }

    private fun findUser(nameOrEmail: String): User? =
            users.find { user -> user.name == nameOrEmail || user.email == nameOrEmail }

    private fun loginError(nameOrEmail: String): Promise<Credentials> =
            Promise.reject(RuntimeException("Unable to authenticate user '$nameOrEmail'"))

    private fun assertUserNameDoesNotExist(name: String) {
        if (users.find { it.name == name } != null) {
            throw RuntimeException("User named '$name' already exists")
        }
    }

    private fun assertUserEmailDoesNotExist(email: String) {
        if (users.find { it.email == email } != null) {
            throw RuntimeException("User with email '$email' already exists")
        }
    }

    private fun createUser(name: String, email: String, password: String): User {
        val user = User(name, email, password)
        users.add(user)
        return user
    }
}
