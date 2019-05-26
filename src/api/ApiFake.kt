package api

import clock.Clock
import model.*
import model.StringConversions.clean
import kotlin.js.Date
import kotlin.js.Promise

class ApiFake(private val clock: Clock) : Api {
    private val users = mutableListOf<User>()
    private val elections = mutableListOf<Election>()
    private val allBallots: MutableList<Ballot> = mutableListOf()
    private val candidatesByElection: MutableMap<String, List<String>> = mutableMapOf()
    private val votersByElection: MutableMap<String, List<String>> = mutableMapOf()

    private data class ElectionAndIndex(val election: Election, val index: Int)

    init {
        // credentials
        val aliceCredentials = Credentials("Alice", "password")
        val bobCredentials = Credentials("Bob", "password")
        val carolCredentials = Credentials("Carol", "password")
        val daveCredentials = Credentials("Dave", "password")

        // users
        register("Alice", "alice@email.com", "password")
        register("Bob", "bob@email.com", "password")
        register("Carol", "carol@email.com", "password")
        register("Dave", "dave@email.com", "password")

        // Favorite Ice Cream
        createElection(aliceCredentials, "Favorite Ice Cream")
        updateCandidates(aliceCredentials, "Favorite Ice Cream", listOf("Chocolate", "Vanilla", "Strawberry"))
        updateEligibleVoters(aliceCredentials, "Favorite Ice Cream", listOf("Alice", "Carol"))

        // Government
        createElection(aliceCredentials, "Government")
        updateCandidates(aliceCredentials, "Government", listOf("Monarchy", "Aristocracy", "Democracy"))
        updateEligibleVoters(aliceCredentials, "Government", listOf("Alice", "Bob", "Carol", "Dave"))
        doneEditingElection(aliceCredentials, "Government")

        // Dystopia
        createElection(aliceCredentials, "Dystopia")
        updateCandidates(aliceCredentials, "Dystopia", listOf("1984", "Fahrenheit 451", "Brave New World"))
        updateEligibleVoters(aliceCredentials, "Dystopia", listOf("Alice", "Bob", "Carol", "Dave"))
        doneEditingElection(aliceCredentials, "Dystopia")

        // Pet
        createElection(bobCredentials, "Pet")
        updateCandidates(bobCredentials, "Pet", listOf("Cat", "Dog", "Bird", "Fish", "Reptile"))
        updateEligibleVoters(bobCredentials, "Pet", listOf("Alice", "Bob", "Dave"))
        doneEditingElection(bobCredentials, "Pet")

        // Science Fiction
        createElection(carolCredentials, "Science Fiction")
        updateCandidates(carolCredentials, "Science Fiction", listOf("Babylon 5", "Star Trek", "Blake's 7", "Firefly"))
        updateEligibleVoters(carolCredentials, "Science Fiction", listOf("Alice", "Bob", "Carol", "Dave"))

        // Fantasy
        createElection(daveCredentials, "Fantasy")
        updateCandidates(daveCredentials, "Fantasy", listOf("Marvel Cinematic Universe", "Lord of the Rings", "Harry Potter"))
        updateEligibleVoters(daveCredentials, "Fantasy", listOf("Alice", "Bob", "Carol", "Dave"))
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
                    createBallots(electionName)
                    election.startNow()
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

    private fun createBallots(electionName: String) {
        val voters = votersByElection.getValue(electionName)
        val candidates = candidatesByElection.getValue(electionName)
        val election = findElectionByName(electionName)
        for (voter in voters) {
            createBallot(election, voter, candidates)
        }
    }

    private fun createBallot(election: Election, voterName: String, candidates: List<String>) {
        val whenCast = null
        val isActive = election.end == null || clock.now().getTime() < election.end.getTime()
        val rankings = createRankings(candidates)
        val ballot = Ballot(
                election.name,
                voterName,
                whenCast,
                isActive,
                rankings)
        allBallots.add(ballot)
    }

    private fun createRankings(candidates: List<String>): List<Ranking> = candidates.map(::createRanking)

    private fun createRanking(candidateName: String): Ranking = Ranking(rank = null, candidateName = candidateName)

    private fun <T> handleException(f: () -> T): Promise<T> =
            try {
                Promise.resolve(f())
            } catch (ex: RuntimeException) {
                Promise.reject(ex)
            }
}
