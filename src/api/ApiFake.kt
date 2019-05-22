package api

import model.*
import kotlin.js.Date
import kotlin.js.Promise

class ApiFake : Api {
    private val users = mutableListOf<User>()
    private val elections = mutableListOf<Election>()
    private val allBallots: MutableList<Ballot> = mutableListOf()
    private val candidatesByElection: MutableMap<String, List<String>> = mutableMapOf()

    private data class ElectionAndIndex(val election: Election, val index: Int)

    init {
        val aliceCredentials = Credentials("Alice", "password")
        val bobCredentials = Credentials("Bob", "password")
        val iceCreamElectionName = "Favorite Ice Cream"
        register(aliceCredentials.name, "alice@email.com", aliceCredentials.password)
        register(bobCredentials.name, "bob@email.com", bobCredentials.password)
        register("Carol", "carol@email.com", "password")
        register("Dave", "dave@email.com", "password")
        createElection(aliceCredentials.name, iceCreamElectionName)
        updateCandidates(aliceCredentials, iceCreamElectionName, listOf("Chocolate", "Vanilla", "Strawberry"))
        createElection(aliceCredentials, "Election 2")
        createElection("Bob", "Election 3")
    }

    override fun login(nameOrEmail: String, password: String): Promise<Credentials> =
            handleException {
                val user = searchUser(nameOrEmail)
                when {
                    user == null -> throw RuntimeException("Unable to authenticate user '$nameOrEmail'")
                    user.password == password -> Credentials(user.name, user.password)
                    else -> throw RuntimeException("Unable to authenticate user '$nameOrEmail'")
                }
            }

    override fun register(name: String, email: String, password: String): Promise<Credentials> =
            handleException {
                assertUserNameDoesNotExist(name)
                assertUserEmailDoesNotExist(email)
                val user = createUser(name, email, password)
                Credentials(user.name, user.password)
            }

    override fun createElection(credentials: Credentials, electionName: String): Promise<Election> =
            handleException {
                assertAllowedToCreateElection(credentials)
                assertElectionNameDoesNotExist(electionName)
                val election = createElection(credentials.name, electionName)
                election
            }

    override fun copyElection(credentials: Credentials,
                              newElectionName: String,
                              electionToCopyName: String): Promise<Unit> =
            handleException {
                TODO("not implemented - copyElection")
            }

    override fun listElections(credentials: Credentials): Promise<List<Election>> =
            handleException {
                elections
            }

    override fun getElection(credentials: Credentials, electionName: String): Promise<Election> =
            handleException {
                assertAllowedToViewElection(credentials, electionName)
                val election = findElectionByName(electionName)
                election
            }

    override fun doneEditingElection(credentials: Credentials, electionName: String): Promise<Unit> =
            handleException {
                TODO("not implemented - doneEditingElection")
            }

    override fun startElection(credentials: Credentials, electionName: String): Promise<Unit> =
            handleException {
                TODO("not implemented - startElection")
            }

    override fun endElection(credentials: Credentials, electionName: String): Promise<Unit> =
            handleException {
                TODO("not implemented - endElection")
            }

    override fun listCandidates(credentials: Credentials, electionName: String): Promise<List<String>> =
            handleException {
                assertAllowedToViewElection(credentials, electionName)
                candidatesByElection.getValue(electionName)
            }

    override fun updateCandidates(credentials: Credentials, electionName: String, candidates: List<String>): Promise<Unit> =
            handleException {
                val election = findElectionByName(electionName)
                assertAllowedToEditElection(credentials, election)
                candidatesByElection[electionName] = candidates
                updateElection(credentials, electionName) { election ->
                    election.copy(candidateCount = candidates.size)
                }
                Unit
            }

    override fun listEligibleVoters(credentials: Credentials, electionName: String): Promise<List<String>> =
            handleException {
                TODO("not implemented - listEligibleVoters")
            }

    override fun listAllVoters(credentials: Credentials): Promise<List<String>> =
            handleException {
                TODO("not implemented - listAllVoters")
            }

    override fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoters: List<String>): Promise<Unit> =
            handleException {
                TODO("not implemented - updateEligibleVoters")
            }

    override fun listBallots(credentials: Credentials, voterName: String): Promise<List<Ballot>> =
            handleException {
                assertCredentialsValid(credentials)
                assertVoterNameExists(voterName)
                assertAllowedToSeeBallotsFor(credentials, voterName)
                val ballotsForVoter = allBallots.filter { ballot ->
                    ballot.voterName == voterName
                }
                ballotsForVoter
            }

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Promise<Ballot> =
            handleException {
                TODO("not implemented - getBallot")
            }

    override fun castBallot(credentials: Credentials, electionName: String, voterName: String, ballot: Ballot): Promise<Unit> =
            handleException {
                TODO("not implemented - castBallot")
            }

    override fun setStartDate(credentials: Credentials, electionName: String, isoStartDate: String?): Promise<Election> =
            handleException {
                updateElection(credentials, electionName) { election ->
                    val date = if (isoStartDate == null) null else Date(isoStartDate)
                    election.copy(start = date)
                }
            }

    override fun setEndDate(credentials: Credentials, electionName: String, isoEndDate: String?): Promise<Election> =
            handleException {
                updateElection(credentials, electionName) { election ->
                    val date = if (isoEndDate == null) null else Date(isoEndDate)
                    election.copy(end = date)
                }
            }

    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): Promise<Election> =
            handleException {
                updateElection(credentials, electionName) { election ->
                    election.copy(secretBallot = secretBallot)
                }
            }

    private fun updateElection(credentials: Credentials, electionName: String, update: (Election) -> Election): Election {
        val (election, index) = findElectionAndIndexByName(electionName)
        assertAllowedToEditElection(credentials, election)
        val updatedElection = update(election)
        elections[index] = updatedElection
        return updatedElection
    }

    private fun searchUser(nameOrEmail: String): User? =
            users.find { user -> user.name == nameOrEmail || user.email == nameOrEmail }

    private fun findUserByName(name: String): User =
            searchUserByName(name) ?: throw RuntimeException("User with name '$name' not found")

    private fun searchUserByName(name: String): User? = users.find { user -> user.name == name }

    private fun findElectionByName(electionName: String): Election =
            searchElectionByName(electionName) ?: throw RuntimeException("Election with name '$electionName' not found")

    private fun searchElectionByName(electionName: String): Election? =
            searchElectionAndIndexByName(electionName)?.election

    private fun searchElectionAndIndexByName(electionName: String): ElectionAndIndex? {
        val index = elections.indexOfFirst { election -> election.name == electionName }
        return if (index == -1) null else ElectionAndIndex(elections[index], index)
    }

    private fun findElectionAndIndexByName(electionName: String): ElectionAndIndex =
            searchElectionAndIndexByName(electionName)
                    ?: throw RuntimeException("Election with name '$electionName' not found")

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

    private fun assertAllowedToViewElection(credentials: Credentials, electionName: String) {
        val user = authenticateUser(credentials)
        if (user.authorization.canViewAllElections) {
            return
        } else {
            val election = findElectionByName(electionName)
            if (election.ownerName != user.name) {
                throw RuntimeException("User '${user.name}' is not allowed view election '$electionName'")
            }
        }
    }

    private fun assertAllowedToEditElection(credentials: Credentials, election: Election) {
        assertCredentialsValid(credentials)
        if (credentials.name != election.ownerName) {
            throw RuntimeException("User '${credentials.name}' is not allowed to edit election '${election.name}' owned by ${election.ownerName}")
        }
    }

    private fun assertCredentialsValid(credentials: Credentials) {
        val user = findUserByName(credentials.name)
        if (credentials.password != user.password) {
            throw RuntimeException("Incorrect password for user ${credentials.name}")
        }
    }

    private fun assertVoterNameExists(voterName: String) {
        findUserByName(voterName)
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
        val candidates = mutableListOf<String>()
        candidatesByElection[electionName] = candidates
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

    private fun <T> handleException(f: () -> T): Promise<T> =
            try {
                Promise.resolve(f())
            } catch (ex: RuntimeException) {
                Promise.reject(ex)
            }
}
