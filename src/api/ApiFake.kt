package api

import model.*
import kotlin.js.Date
import kotlin.js.Promise

class ApiFake : Api {
    private val users: MutableList<User> = mutableListOf(
            User("Alice", "alice@email.com", "password", Standard),
            User("Bob", "bob@email.com", "password", Standard),
            User("Carol", "carol@email.com", "password", Standard),
            User("Dave", "dave@email.com", "password", Standard),
            User("foo", "foo@email.com", "bar", Standard))
    private val elections: MutableList<Election> = mutableListOf(
            Election("Alice", "Election 1"),
            Election("Alice", "Election 2"),
            Election("Bob", "Election 3"))
    private val allBallots: MutableList<Ballot> = mutableListOf()

    override fun login(nameOrEmail: String, password: String): Promise<Credentials> {
        val user = searchUser(nameOrEmail)
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

    override fun createElection(credentials: Credentials, electionName: String): Promise<Election> {
        return try {
            assertAllowedToCreateElection(credentials)
            assertElectionNameDoesNotExist(electionName)
            val election = createElection(credentials.name, electionName)
            Promise.resolve(election)
        } catch (ex: RuntimeException) {
            Promise.reject(ex)
        }
    }

    override fun copyElection(credentials: Credentials,
                              newElectionName: String,
                              electionToCopyName: String): Promise<Unit> {
        TODO("not implemented - copyElection")
    }

    override fun listElections(credentials: Credentials): Promise<List<Election>> {
        return Promise.resolve(elections)
    }

    override fun getElection(credentials: Credentials, electionName: String): Promise<Election> {
        return try {
            assertAllowedToGetElection(credentials, electionName)
            val election = lookupElectionByName(electionName)
            Promise.resolve(election)
        } catch (ex: RuntimeException) {
            Promise.reject(ex)
        }
    }

    override fun doneEditingElection(credentials: Credentials, electionName: String): Promise<Unit> {
        TODO("not implemented - doneEditingElection")
    }

    override fun startElection(credentials: Credentials, electionName: String): Promise<Unit> {
        TODO("not implemented - startElection")
    }

    override fun endElection(credentials: Credentials, electionName: String): Promise<Unit> {
        TODO("not implemented - endElection")
    }

    override fun listCandidates(credentials: Credentials, electionName: String): Promise<List<String>> {
        TODO("not implemented - listCandidates")
    }

    override fun updateCandidates(credentials: Credentials, electionName: String, candidates: List<String>): Promise<Unit> {
        TODO("not implemented - updateCandidates")
    }

    override fun listEligibleVoters(credentials: Credentials, electionName: String): Promise<List<String>> {
        TODO("not implemented - listEligibleVoters")
    }

    override fun listAllVoters(credentials: Credentials): Promise<List<String>> {
        TODO("not implemented - listAllVoters")
    }

    override fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoters: List<String>): Promise<Unit> {
        TODO("not implemented - updateEligibleVoters")
    }

    override fun listBallots(credentials: Credentials, voterName: String): Promise<List<Ballot>> {
        return try {
            assertCredentialsValid(credentials)
            assertVoterNameExists(voterName)
            assertAllowedToSeeBallotsFor(credentials, voterName)
            val ballotsForVoter = allBallots.filter { ballot ->
                ballot.voterName == voterName
            }
            Promise.resolve(ballotsForVoter)
        } catch (ex: RuntimeException) {
            Promise.reject(ex)
        }
    }

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Promise<Ballot> {
        TODO("not implemented - getBallot")
    }

    override fun castBallot(credentials: Credentials, electionName: String, voterName: String, ballot: Ballot): Promise<Unit> {
        TODO("not implemented - castBallot")
    }

    override fun setStartDate(credentials: Credentials, electionName: String, isoStartDate: String): Promise<Election> {
        return try {
            val user = authenticateUser(credentials)
            val electionIndex = lookupElectionIndexByName(electionName)
            val election = elections[electionIndex]
            assertAllowedToEditElection(user, election)
            val updatedElection = election.copy(start = Date(isoStartDate))
            elections[electionIndex] = updatedElection
            return Promise.Companion.resolve(updatedElection)
        } catch (ex: RuntimeException) {
            Promise.reject(ex)
        }
    }

    private fun lookupUser(nameOrEmail: String): User =
            searchUser(nameOrEmail) ?: throw RuntimeException("User with name or email '$nameOrEmail' not found")

    private fun searchUser(nameOrEmail: String): User? =
            users.find { user -> user.name == nameOrEmail || user.email == nameOrEmail }

    private fun lookupUserByName(name: String): User =
            searchUserByName(name) ?: throw RuntimeException("User with name '$name' not found")

    private fun searchUserByName(name: String): User? = users.find { user -> user.name == name }

    private fun lookupElectionByName(electionName: String): Election =
            searchElectionByName(electionName) ?: throw RuntimeException("Election with name '$electionName' not found")

    private fun searchElectionByName(electionName: String): Election? {
        val index = searchElectionIndexByName(electionName)
        return if (index == null) null else elections[index]
    }

    private fun searchElectionIndexByName(electionName: String): Int? {
        val index = elections.indexOfFirst { election -> election.name == electionName }
        return if (index == -1) null else index
    }

    private fun lookupElectionIndexByName(electionName: String): Int =
            searchElectionIndexByName(electionName)
                    ?: throw RuntimeException("Election with name '$electionName' not found")

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

    private fun assertElectionNameDoesNotExist(name: String) {
        if (elections.find { it.name == name } != null) {
            throw RuntimeException("Election named '$name' already exists")
        }
    }

    private fun assertAllowedToCreateElection(credentials: Credentials) {
        val user = authenticateUser(credentials)
        if (!user.authorization.canCreateElections) {
            throw RuntimeException("User '${user.name} is not allowed to create elections")
        }
    }

    private fun assertAllowedToGetElection(credentials: Credentials, electionName: String) {
        val user = authenticateUser(credentials)
        if (user.authorization.canViewAllElections) {
            return
        } else {
            val election = lookupElectionByName(electionName)
            if (election.ownerName != user.name) {
                throw RuntimeException("User '${user.name}' is not allowed view election '$electionName'")
            }
        }
    }

    private fun assertAllowedToEditElection(user: User, election: Election) {
        if (election.ownerName != user.name) {
            throw RuntimeException("User '${user.name}' is not allowed to edit election '${election.name}' owned by ${election.ownerName}")
        }
    }

    private fun assertCredentialsValid(credentials: Credentials) {
        val user = lookupUserByName(credentials.name)
        if (credentials.password != user.password) {
            throw RuntimeException("Incorrect password for user ${credentials.name}")
        }
    }

    private fun assertVoterNameExists(voterName: String) {
        lookupUserByName(voterName)
    }

    private fun assertAllowedToSeeBallotsFor(credentials: Credentials, voterName: String) {
        if (credentials.name != voterName) {
            throw RuntimeException("User ${credentials.name} not allowed to see votes for $voterName")
        }
    }

    private fun createUser(name: String, email: String, password: String): User {
        val user = User(name, email, password, Standard)
        users.add(user)
        return user
    }

    private fun createElection(ownerName: String, electionName: String): Election {
        val election = Election(ownerName, electionName)
        elections.add(election)
        return election
    }

    private fun authenticateUser(credentials: Credentials): User {
        val user = searchUser(credentials.name)
        when {
            user == null -> throw RuntimeException("Invalid credentials for user '${credentials.name}'")
            user.password == credentials.password -> return user
            else -> throw RuntimeException("Invalid credentials for user '${credentials.name}'")
        }
    }
}
