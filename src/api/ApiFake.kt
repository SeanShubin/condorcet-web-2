package api

import model.*
import model.StringConversions.clean
import kotlin.js.Date
import kotlin.js.Promise

class ApiFake : Api {
    private val users = mutableListOf<User>()
    private val elections = mutableListOf<Election>()
    private val allBallots: MutableList<Ballot> = mutableListOf()
    private val candidatesByElection: MutableMap<String, List<String>> = mutableMapOf()
    private val votersByElection: MutableMap<String, List<String>> = mutableMapOf()

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
        updateEligibleVoters(aliceCredentials, iceCreamElectionName, listOf("Alice", "Carol"))
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
                val cleanName = name.clean()
                assertUserNameDoesNotExist(cleanName)
                assertUserEmailDoesNotExist(email)
                val user = createUser(cleanName, email, password)
                Credentials(user.name, user.password)
            }

    override fun createElection(credentials: Credentials, electionName: String): Promise<Election> =
            handleException {
                val cleanElectionName = electionName.clean()
                assertCredentialsValid(credentials)
                assertElectionNameDoesNotExist(cleanElectionName)
                val election = createElection(credentials.name, cleanElectionName)
                election
            }

    override fun copyElection(credentials: Credentials,
                              newElectionName: String,
                              electionToCopyName: String): Promise<Election> =
            handleException {
                val cleanNewElectionName = newElectionName.clean()
                val user = assertCredentialsValid(credentials)
                val electionToCopy = findElectionByName(electionToCopyName)
                val election = Election(
                        ownerName = user.name,
                        name = cleanNewElectionName,
                        start = electionToCopy.start,
                        end = electionToCopy.end,
                        secretBallot = electionToCopy.secretBallot,
                        status = Election.ElectionStatus.EDITING,
                        candidateCount = electionToCopy.candidateCount,
                        voterCount = electionToCopy.voterCount)
                candidatesByElection[cleanNewElectionName] = candidatesByElection.getValue(electionToCopyName)
                votersByElection[cleanNewElectionName] = votersByElection.getValue(electionToCopyName)
                elections.add(election)
                election
            }

    override fun listElections(credentials: Credentials): Promise<List<Election>> =
            handleException {
                assertCredentialsValid(credentials)
                elections
            }

    override fun getElection(credentials: Credentials, electionName: String): Promise<Election> =
            handleException {
                assertCredentialsValid(credentials)
                val election = findElectionByName(electionName)
                election
            }

    override fun doneEditingElection(credentials: Credentials, electionName: String): Promise<Election> =
            handleException {
                updateElection(credentials, electionName) { election ->
                    election.doneEditing()
                }
            }

    override fun startElection(credentials: Credentials, electionName: String): Promise<Election> =
            handleException {
                updateElection(credentials, electionName) { election ->
                    election.startNow()
                }
            }

    override fun endElection(credentials: Credentials, electionName: String): Promise<Election> =
            handleException {
                updateElection(credentials, electionName) { election ->
                    election.endNow()
                }
            }

    override fun listCandidates(credentials: Credentials, electionName: String): Promise<List<String>> =
            handleException {
                assertCredentialsValid(credentials)
                candidatesByElection.getValue(electionName)
            }

    override fun updateCandidates(credentials: Credentials, electionName: String, candidates: List<String>): Promise<List<String>> =
            handleException {
                val user = assertCredentialsValid(credentials)
                val election = findElectionByName(electionName)
                assertUserOwnsElection(user, election)
                val cleanCandidates = candidates.clean()
                candidatesByElection[electionName] = cleanCandidates
                updateElection(credentials, electionName) { it.copy(candidateCount = cleanCandidates.size) }
                cleanCandidates
            }

    override fun listEligibleVoters(credentials: Credentials, electionName: String): Promise<Voters> =
            handleException {
                assertCredentialsValid(credentials)
                val voterList = votersByElection.getValue(electionName)
                val isAllVoters = voterList.size == users.size
                Voters(voterList, isAllVoters)
            }

    override fun listAllVoters(credentials: Credentials): Promise<List<String>> =
            handleException {
                assertCredentialsValid(credentials)
                users.map { it.name }
            }

    override fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoters: List<String>): Promise<Voters> =
            handleException {
                val user = assertCredentialsValid(credentials)
                val election = findElectionByName(electionName)
                assertUserOwnsElection(user, election)
                val cleanVoters = eligibleVoters.clean()
                votersByElection[electionName] = cleanVoters
                updateElection(credentials, electionName) { it.copy(voterCount = cleanVoters.size) }
                val isAllVoters = cleanVoters.size == users.size
                Voters(cleanVoters, isAllVoters)
            }

    override fun updateEligibleVotersToAll(credentials: Credentials, electionName: String): Promise<Voters> =
            handleException {
                val user = assertCredentialsValid(credentials)
                val election = findElectionByName(electionName)
                assertUserOwnsElection(user, election)
                val cleanVoters = users.map { it.name }.clean()
                votersByElection[electionName] = cleanVoters
                updateElection(credentials, electionName) { it.copy(voterCount = cleanVoters.size) }
                val isAllVoters = cleanVoters.size == users.size
                Voters(cleanVoters, isAllVoters)
            }

    override fun listBallots(credentials: Credentials, voterName: String): Promise<List<Ballot>> =
            handleException {
                val user = assertCredentialsValid(credentials)
                val voter = findUserByName(voterName)
                assertUserIsVoter(user, voter)
                val ballotsForVoter = allBallots.filter { ballot ->
                    ballot.voterName == voterName
                }
                ballotsForVoter
            }

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Promise<Ballot> =
            handleException {
                val user = assertCredentialsValid(credentials)
                val voter = findUserByName(voterName)
                assertUserIsVoter(user, voter)
                TODO("not implemented - getBallot")
            }

    override fun castBallot(credentials: Credentials, electionName: String, voterName: String, ballot: Ballot): Promise<Unit> =
            handleException {
                val user = assertCredentialsValid(credentials)
                val voter = findUserByName(voterName)
                assertUserIsVoter(user, voter)
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
        val user = assertCredentialsValid(credentials)
        assertUserOwnsElection(user, election)
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

    private fun assertUserIsVoter(user: User, voter: User) {
        if (user.name != voter.name) {
            throw RuntimeException("User '${user.name}' is not allowed view details for '${voter.name}'")
        }
    }

    private fun assertUserOwnsElection(user: User, election: Election) {
        if (user.name != election.ownerName) {
            throw RuntimeException("User '${user.name}' is not allowed to edit election '${election.name}' owned by ${election.ownerName}")
        }
    }

    private fun assertCredentialsValid(credentials: Credentials): User {
        val user = findUserByName(credentials.name)
        if (credentials.password != user.password) {
            throw RuntimeException("Incorrect password for user ${credentials.name}")
        }
        return user
    }

    private fun createUser(name: String, email: String, password: String): User {
        val user = User(name, email, password)
        users.add(user)
        return user
    }

    private fun createElection(ownerName: String, electionName: String): Election {
        val election = Election(ownerName, electionName)
        elections.add(election)
        candidatesByElection[electionName] = emptyList()
        votersByElection[electionName] = emptyList()
        return election
    }

    private fun <T> handleException(f: () -> T): Promise<T> =
            try {
                Promise.resolve(f())
            } catch (ex: RuntimeException) {
                Promise.reject(ex)
            }
}
