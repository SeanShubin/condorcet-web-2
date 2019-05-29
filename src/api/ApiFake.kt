package api

import clock.Clock
import db.Db
import model.*
import util.ListUtil.nullOrOne
import util.StringConversions.clean
import kotlin.js.Date
import kotlin.js.Promise

class ApiFake(private val clock: Clock, private val db: Db) : Api {
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

        // Voting
        castBallot(aliceCredentials, "Favorite Ice Cream", "Alice", listOf(
                Ranking(1, "Vanilla"),
                Ranking(2, "Chocolate"),
                Ranking(3, "Strawberry")
        ))
        castBallot(aliceCredentials, "Government", "Alice", listOf(
                Ranking(1, "Aristocracy"),
                Ranking(2, "Monarchy"),
                Ranking(3, "Democracy")
        ))
    }

    override fun login(nameOrEmail: String, password: String): Promise<Credentials> =
            handleException {
                val user = searchUserByNameOrEmail(nameOrEmail)
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
                val user = Db.User(cleanName, email, password)
                db.user.add(user)
                Credentials(user.name, user.password)
            }

    override fun createElection(credentials: Credentials, electionName: String): Promise<Election> =
            handleException {
                val cleanElectionName = electionName.clean()
                assertCredentialsValid(credentials)
                assertElectionNameDoesNotExist(cleanElectionName)
                val owner = credentials.name
                val end = null
                val secret = true
                val status = Db.Status.EDITING
                val election = Db.Election(owner, cleanElectionName, end, secret, status)
                db.election.add(election)
                election.toApi()
            }

    override fun copyElection(credentials: Credentials,
                              newElectionName: String,
                              electionToCopyName: String): Promise<Election> =
            handleException {
                val cleanNewElectionName = newElectionName.clean()
                assertCredentialsValid(credentials)
                copyDbElection(electionToCopyName, cleanNewElectionName, credentials.name)
                copyDbElectionCandidates(electionToCopyName, cleanNewElectionName)
                copyDbElectionVoters(electionToCopyName, cleanNewElectionName)
                val newElection = db.election.find(cleanNewElectionName)
                newElection.toApi()
            }


    override fun listElections(credentials: Credentials): Promise<List<Election>> =
            handleException {
                assertCredentialsValid(credentials)
                db.election.listAll().map { it.toApi() }
            }


    override fun getElection(credentials: Credentials, electionName: String): Promise<Election> =
            handleException {
                assertCredentialsValid(credentials)
                db.election.find(electionName).toApi()
            }

    override fun doneEditingElection(credentials: Credentials, electionName: String): Promise<Election> =
            handleException {
                updateElection(credentials, electionName) { election ->
                    election.copy(status = Db.Status.LIVE)
                }
            }

    override fun endElection(credentials: Credentials, electionName: String): Promise<Election> =
            handleException {
                updateElection(credentials, electionName) { election ->
                    election.copy(status = Db.Status.COMPLETE)
                }
            }

    override fun listCandidates(credentials: Credentials, electionName: String): Promise<List<String>> =
            handleException {
                assertCredentialsValid(credentials)
                db.candidate.listWhere { it.electionName == electionName }.map { it.name }
            }

    override fun updateCandidates(credentials: Credentials, electionName: String, candidates: List<String>): Promise<List<String>> =
            handleException {
                assertCredentialsValid(credentials)
                assertUserOwnsElection(credentials.name, electionName)
                val cleanCandidates = candidates.clean()
                db.candidate.removeWhere { it.electionName == electionName }
                val newCandidates = cleanCandidates.map { Db.Candidate(it, electionName) }
                db.candidate.addAll(newCandidates)
                cleanCandidates
            }


    override fun listEligibleVoters(credentials: Credentials, electionName: String): Promise<Voters> =
            handleException {
                assertCredentialsValid(credentials)
                val dbVoterList = db.voter.listWhere { it.electionName == electionName }
                val isAllVoters = dbVoterList.size == db.user.countAll()
                val apiVoterList = dbVoterList.map { it.userName }
                Voters(apiVoterList, isAllVoters)
            }

    override fun listAllVoters(credentials: Credentials): Promise<List<String>> =
            handleException {
                assertCredentialsValid(credentials)
                db.user.listAll().map { it.name }
            }

    override fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoters: List<String>): Promise<Voters> =
            handleException {
                assertCredentialsValid(credentials)
                assertUserOwnsElection(credentials.name, electionName)
                val cleanVoters = eligibleVoters.clean()
                db.voter.removeWhere { it.electionName == electionName }
                val newVoters = cleanVoters.map { Db.Voter(it, electionName) }
                db.voter.addAll(newVoters)
                val isAllVoters = cleanVoters.size == db.user.countAll()
                Voters(cleanVoters, isAllVoters)
            }

    override fun updateEligibleVotersToAll(credentials: Credentials, electionName: String): Promise<Voters> =
            handleException {
                assertCredentialsValid(credentials)
                assertUserOwnsElection(credentials.name, electionName)
                db.voter.removeWhere { it.electionName == electionName }
                val allVoters = db.user.listAll().map { Db.Voter(it.name, electionName) }
                db.voter.addAll(allVoters)
                val isAllVoters = true
                val voterNames = allVoters.map { it.userName }
                Voters(voterNames, isAllVoters)
            }

    override fun listBallots(credentials: Credentials, voterName: String): Promise<List<Ballot>> =
            handleException {
                assertCredentialsValid(credentials)
                assertUserIsVoter(credentials.name, voterName)
                val ballots = db.ballot.listWhere { it.voterName == voterName }
                val now = clock.now()
                ballots.map { it.toApi(now) }
            }

    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Promise<Ballot> =
            handleException {
                assertCredentialsValid(credentials)
                assertUserIsVoter(credentials.name, voterName)
                val now = clock.now()
                db.ballot.find(Db.Voter(voterName, electionName)).toApi(now)
            }

    override fun castBallot(credentials: Credentials,
                            electionName: String,
                            voterName: String,
                            rankings: List<Ranking>): Promise<Ballot> =
            handleException {
                assertCredentialsValid(credentials)
                assertUserIsVoter(credentials.name, voterName)
                assertVoterIsEligibleToVoteInElection(voterName, electionName)
                assertElectionIsActive(electionName, clock.now())
                val now = clock.now()
                db.ranking.removeWhere { it.electionName === electionName && it.voterName == voterName }
                val newRankings = rankings.map { it.toDb(electionName, voterName) }
                db.ranking.addAll(newRankings)
                val ballot = Db.Ballot(voterName, electionName, now.toISOString())
                db.ballot.addOrUpdate(ballot)
                ballot.toApi(now)
            }

    override fun setEndDate(credentials: Credentials, electionName: String, isoEndDate: String?): Promise<Election> =
            handleException {
                updateElection(credentials, electionName) { election ->
                    election.copy(end = isoEndDate)
                }
            }

    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): Promise<Election> =
            handleException {
                updateElection(credentials, electionName) { election ->
                    election.copy(secret = secretBallot)
                }
            }

    private fun updateElection(credentials: Credentials, electionName: String, update: (Db.Election) -> Db.Election): Election {
        val oldElection = db.election.find(electionName)
        assertCredentialsValid(credentials)
        assertUserOwnsElection(credentials.name, oldElection)
        val newElection = update(oldElection)
        db.election.update(newElection)
        return newElection.toApi()
    }

    private fun assertUserIsVoter(userName: String, voterName: String) {
        if (userName != voterName) {
            throw RuntimeException("User '$userName' is not allowed view details for '$voterName'")
        }
    }

    private fun assertVoterIsEligibleToVoteInElection(voterName: String, electionName: String) {
        val eligibleVoters = db.voter.listWhere { it.electionName == electionName }.map { it.userName }
        if (!eligibleVoters.contains(voterName)) {
            throw RuntimeException("Voter '$voterName' is not eligible to vote in election '$electionName'")
        }
    }

    private fun assertUserOwnsElection(name: String, election: Db.Election) {
        if (name != election.owner) {
            throw RuntimeException("User '$name' is not allowed to edit election '${election.name}' owned by ${election.owner}")
        }
    }

    private fun assertUserOwnsElection(name: String, electionName: String) {
        val election = db.election.find(electionName)
        assertUserOwnsElection(name, election)
    }

    private fun Db.Election.toApi(): Election =
            Election(
                    ownerName = owner,
                    name = name,
                    end = end?.toDate(),
                    secretBallot = secret,
                    status = status.toApiStatus(),
                    candidateCount = db.candidate.countWhere { it.electionName == name },
                    voterCount = db.voter.countWhere { it.electionName == name }
            )

    private fun Db.Ballot.toApi(asOf: Date): Ballot {
        val rankings = db.ranking.listWhere { it.electionName == this.electionName && it.voterName == this.voterName }
        val apiRankings = rankings.map { it.toApi() }
        val isActive = isElectionActive(electionName, asOf)
        return Ballot(electionName, voterName, whenCast?.toDate(), isActive, apiRankings)
    }

    private fun Db.Ranking.toApi(): Ranking = Ranking(rank, candidateName)

    private fun Ranking.toDb(electionName: String, voterName: String): Db.Ranking = Db.Ranking(
            voterName,
            electionName,
            candidateName,
            rank)

    private fun Db.Status.toApiStatus(): Election.ElectionStatus =
            when (this) {
                Db.Status.EDITING -> Election.ElectionStatus.EDITING
                Db.Status.LIVE -> Election.ElectionStatus.LIVE
                Db.Status.COMPLETE -> Election.ElectionStatus.CONCLUDED
            }

    private fun String.toDate(): Date = Date(this)

    private fun searchUserByNameOrEmail(nameOrEmail: String): Db.User? {
        val users = db.user.listWhere { it.name == nameOrEmail || it.email == nameOrEmail }
        return users.nullOrOne()
    }

    private fun assertUserNameDoesNotExist(name: String) {
        if (db.user.existsWhere { it.name == name }) {
            throw RuntimeException("User with name '$name' already exists")
        }
    }

    private fun assertUserEmailDoesNotExist(email: String) {
        if (db.user.existsWhere { it.email == email }) {
            throw RuntimeException("User with email '$email' already exists")
        }
    }

    private fun assertCredentialsValid(credentials: Credentials) {
        val user = db.user.find(credentials.name)
        if (user.password != credentials.password) {
            throw RuntimeException("Unable to authenticate user '${credentials.name}'")
        }
    }

    private fun assertElectionNameDoesNotExist(electionName: String) {
        if (db.election.keyExists(electionName)) {
            throw RuntimeException("Election named '$electionName' already exists")
        }
    }

    private fun <T> handleException(f: () -> T): Promise<T> =
            try {
                Promise.resolve(f())
            } catch (ex: RuntimeException) {
                Promise.reject(ex)
            }

    private fun copyDbElection(originalElectionName: String, newElectionName: String, newOwner: String) {
        val originalElection = db.election.find(originalElectionName)
        val newDbElection = Db.Election(
                owner = newOwner,
                name = newElectionName,
                end = null,
                secret = originalElection.secret,
                status = Db.Status.EDITING)
        db.election.add(newDbElection)
    }

    private fun copyDbElectionCandidates(originalElectionName: String, newElectionName: String) {
        val originalCandidates = db.candidate.listWhere { it.electionName == originalElectionName }
        val newCandidates = originalCandidates.map { it.copy(electionName = newElectionName) }
        db.candidate.addAll(newCandidates)
    }

    private fun copyDbElectionVoters(originalElectionName: String, newElectionName: String) {
        val originalVoters = db.voter.listWhere { it.electionName == originalElectionName }
        val newVoters = originalVoters.map { it.copy(electionName = newElectionName) }
        db.voter.addAll(newVoters)
    }

    private fun assertElectionIsActive(electionName: String, asOf: Date) {
        updateActive(electionName, asOf)
        if (!isElectionActive(electionName, asOf)) {
            throw RuntimeException("Election $electionName is not active")
        }
    }

    private fun updateActive(electionName: String, asOf: Date) {
        val election = db.election.find(electionName)
        if (election.status == Db.Status.LIVE) {
            val electionEndTime = election.end
            if (electionEndTime != null) {
                if (asOf.getTime() > electionEndTime.toDate().getTime()) {
                    db.election.update(election.copy(status = Db.Status.COMPLETE))
                }
            }
        }
    }

    private fun isElectionActive(electionName: String, asOf: Date): Boolean {
        updateActive(electionName, asOf)
        val election = db.election.find(electionName)
        return election.status == Db.Status.LIVE
    }
}
