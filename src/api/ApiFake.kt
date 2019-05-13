package api

import model.*
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
        TODO("not implemented - getElection")
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

    private fun findUser(nameOrEmail: String): User? =
            users.find { user -> user.name == nameOrEmail || user.email == nameOrEmail }

    private fun findUserByName(name: String): User? = users.find { user -> user.name == name }

    private fun findVoterByName(name: String): User? = users.find { user -> user.name == name }

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
        val user = lookupUser(credentials)
        if (!user.authorization.canCreateElections()) {
            throw RuntimeException("User '${user.name} is not allowed to create elections")
        }
    }

    private fun assertCredentialsValid(credentials: Credentials) {
        val user = findUserByName(credentials.name)
        if (user == null) {
            throw RuntimeException("User named ${credentials.name} does not exist")
        } else {
            if (credentials.password != user.password) {
                throw RuntimeException("Incorrect password for user ${credentials.name}")
            }
        }
    }

    private fun assertVoterNameExists(voterName: String) {
        if (findVoterByName(voterName) == null) {
            throw RuntimeException("Voter named $voterName does not exist")
        }
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

    private fun lookupUser(credentials: Credentials): User {
        val user = findUser(credentials.name)
        when {
            user == null -> throw RuntimeException("Invalid credentials for user '${credentials.name}'")
            user.password == credentials.password -> return user
            else -> throw RuntimeException("Invalid credentials for user '${credentials.name}'")
        }
    }
}
