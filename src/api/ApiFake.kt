package api

import clock.Clock
import db.Db
import kotlinext.js.getOwnPropertyNames
import kotlinx.html.P
import model.*
import model.Ranking.Companion.normalize
import model.StringConversions.clean
import kotlin.js.Date
import kotlin.js.Promise

class ApiFake(private val clock: Clock, private val db: Db) : Api {
    private data class ElectionAndIndex(val election: Election, val index: Int)
    private data class BallotAndIndex(val ballot: Ballot, val index: Int)

    init {
//        // credentials
//        val aliceCredentials = Credentials("Alice", "password")
//        val bobCredentials = Credentials("Bob", "password")
//        val carolCredentials = Credentials("Carol", "password")
//        val daveCredentials = Credentials("Dave", "password")
//
//        // users
//        register("Alice", "alice@email.com", "password")
//        register("Bob", "bob@email.com", "password")
//        register("Carol", "carol@email.com", "password")
//        register("Dave", "dave@email.com", "password")
//
//        // Favorite Ice Cream
//        createElection(aliceCredentials, "Favorite Ice Cream")
//        updateCandidates(aliceCredentials, "Favorite Ice Cream", listOf("Chocolate", "Vanilla", "Strawberry"))
//        updateEligibleVoters(aliceCredentials, "Favorite Ice Cream", listOf("Alice", "Carol"))
//
//        // Government
//        createElection(aliceCredentials, "Government")
//        updateCandidates(aliceCredentials, "Government", listOf("Monarchy", "Aristocracy", "Democracy"))
//        updateEligibleVoters(aliceCredentials, "Government", listOf("Alice", "Bob", "Carol", "Dave"))
//        doneEditingElection(aliceCredentials, "Government")
//
//        // Dystopia
//        createElection(aliceCredentials, "Dystopia")
//        updateCandidates(aliceCredentials, "Dystopia", listOf("1984", "Fahrenheit 451", "Brave New World"))
//        updateEligibleVoters(aliceCredentials, "Dystopia", listOf("Alice", "Bob", "Carol", "Dave"))
//        doneEditingElection(aliceCredentials, "Dystopia")
//
//
//        // Pet
//        createElection(bobCredentials, "Pet")
//        updateCandidates(bobCredentials, "Pet", listOf("Cat", "Dog", "Bird", "Fish", "Reptile"))
//        updateEligibleVoters(bobCredentials, "Pet", listOf("Alice", "Bob", "Dave"))
//        doneEditingElection(bobCredentials, "Pet")
//
//        // Science Fiction
//        createElection(carolCredentials, "Science Fiction")
//        updateCandidates(carolCredentials, "Science Fiction", listOf("Babylon 5", "Star Trek", "Blake's 7", "Firefly"))
//        updateEligibleVoters(carolCredentials, "Science Fiction", listOf("Alice", "Bob", "Carol", "Dave"))
//
//        // Fantasy
//        createElection(daveCredentials, "Fantasy")
//        updateCandidates(daveCredentials, "Fantasy", listOf("Marvel Cinematic Universe", "Lord of the Rings", "Harry Potter"))
//        updateEligibleVoters(daveCredentials, "Fantasy", listOf("Alice", "Bob", "Carol", "Dave"))
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
                val user = assertCredentialsValid(credentials)
                val electionToCopy = db.election.find(electionToCopyName)
                val newElection = copyDbElection(cleanNewElectionName, electionToCopy)
                copyDbElectionCandidates(cleanNewElectionName, electionToCopy)
                copyDbElectionVoters(cleanNewElectionName, electionToCopy)
                newElection.toApi()

//                val newDbElection =Db.Election(
//                        owner = electionToCopy.owner,
//                        name = newElectionName,
//                        end = null,
//                        secret = electionToCopy.secret,
//                        status = Db.Status.EDITING)
//                val oldDbCandidates = db.candidate.listWhere{ it.electionName == electionToCopyName }
//                val newDbCandidates = oldDbCandidates.map{ it.copy(electionName = electionToCopyName)}
//                val oldDbVoters = db.voter.listWhere { it.electionName == electionToCopyName }
//                val newDbVoters = oldDbVoters.map { it.copy(electionName = electionToCopyName)}
//                db.election.add(newDbElection)
//                db.candidate.add(newDbCandidates)
//                db.voter.add(newDbVoters)
//                val apiElection = Election(
//                        ownerName = user.name,
//                        name = cleanNewElectionName,
//                        end = electionToCopy.end,
//                        secretBallot = electionToCopy.secretBallot,
//                        status = Election.ElectionStatus.EDITING,
//                        candidateCount = electionToCopy.candidateCount,
//                        voterCount = electionToCopy.voterCount)
//                apiElection
            }
//
//    override fun listElections(credentials: Credentials): Promise<List<Election>> =
//            handleException {
//                assertCredentialsValid(credentials)
//                db.election.listAll().map(::dbElectionToApiElection)
//            }
//
//    override fun getElection(credentials: Credentials, electionName: String): Promise<Election> =
//            handleException {
//                assertCredentialsValid(credentials)
//                val election = findElectionByName(electionName)
//                election
//            }
//
//    override fun doneEditingElection(credentials: Credentials, electionName: String): Promise<Election> =
//            handleException {
//                updateElection(credentials, electionName) { election ->
//                    election.startNow()
//                }
//            }
//
//    override fun startElection(credentials: Credentials, electionName: String): Promise<Election> =
//            handleException {
//                updateElection(credentials, electionName) { election ->
//                    election.startNow()
//                }
//            }
//
//    override fun endElection(credentials: Credentials, electionName: String): Promise<Election> =
//            handleException {
//                updateElection(credentials, electionName) { election ->
//                    election.endNow()
//                }
//            }
//
//    override fun listCandidates(credentials: Credentials, electionName: String): Promise<List<String>> =
//            handleException {
//                assertCredentialsValid(credentials)
//                db.candidate.listWhere { election -> election.name == electionName }.map{it.name}
//            }
//
//    override fun updateCandidates(credentials: Credentials, electionName: String, candidates: List<String>): Promise<List<String>> =
//            handleException {
//                val user = assertCredentialsValid(credentials)
//                val election = findElectionByName(electionName)
//                assertUserOwnsElection(user, election)
//                val cleanCandidates = candidates.clean()
//                val existingCandidates = db.candidate.listWhere { it.electionName == electionName }
//                db.candidate.removeAllByValues(existingCandidates)
//                val newCandidates = cleanCandidates.map { Db.Candidate(it, electionName) }
//                db.candidate.add(newCandidates)
//                updateElection(credentials, electionName) { it.copy(candidateCount = cleanCandidates.size) }
//                cleanCandidates
//            }
//
//    override fun listEligibleVoters(credentials: Credentials, electionName: String): Promise<Voters> =
//            handleException {
//                assertCredentialsValid(credentials)
//                val dbVoterList = db.voter.listWhere {it.electionName == electionName}
//                val isAllVoters = dbVoterList.size == db.user.countAll()
//                val apiVoterList = dbVoterList.map { it.userName }
//                Voters(apiVoterList, isAllVoters)
//            }
//
//    override fun listAllVoters(credentials: Credentials): Promise<List<String>> =
//            handleException {
//                assertCredentialsValid(credentials)
//                db.user.listAll().map{it.name}
//            }
//
//    override fun updateEligibleVoters(credentials: Credentials, electionName: String, eligibleVoters: List<String>): Promise<Voters> =
//            handleException {
//                val user = assertCredentialsValid(credentials)
//                val election = findElectionByName(electionName)
//                assertUserOwnsElection(user, election)
//                val cleanVoters = eligibleVoters.clean()
//                val existingVoters = db.voter.listWhere { it.electionName == electionName }
//                db.voter.removeAllByValues(existingVoters)
//                val newVoters = cleanVoters.map { Db.Voter(it, electionName) }
//                db.voter.add(newVoters)
//                updateElection(credentials, electionName) { it.copy(voterCount = cleanVoters.size) }
//                val isAllVoters = cleanVoters.size == db.user.countAll()
//                Voters(cleanVoters, isAllVoters)
//            }
//
//    override fun updateEligibleVotersToAll(credentials: Credentials, electionName: String): Promise<Voters> =
//            handleException {
//                val user = assertCredentialsValid(credentials)
//                val election = findElectionByName(electionName)
//                assertUserOwnsElection(user, election)
//                val cleanVoters = db.user.listAll().map { it.name }.clean()
//
//                votersByElection[electionName] = cleanVoters
//                updateElection(credentials, electionName) { it.copy(voterCount = cleanVoters.size) }
//                val isAllVoters = cleanVoters.size == users.size
//                Voters(cleanVoters, isAllVoters)
//            }
//
//    override fun listBallots(credentials: Credentials, voterName: String): Promise<List<Ballot>> =
//            handleException {
//                val user = assertCredentialsValid(credentials)
//                val voter = findUserByName(voterName)
//                assertUserIsVoter(user, voter)
//                val elections = electionsEligibleForVoter(voter.name)
//                val ballots = elections.map { ballotForElectionAndVoter(it, voter.name) }
//                ballots
//            }
//
//    override fun getBallot(credentials: Credentials, electionName: String, voterName: String): Promise<Ballot> =
//            handleException {
//                val user = assertCredentialsValid(credentials)
//                val voter = findUserByName(voterName)
//                assertUserIsVoter(user, voter)
//
//                TODO("not implemented - getBallot")
//            }
//
//    override fun castBallot(credentials: Credentials,
//                            electionName: String,
//                            voterName: String,
//                            rankings: List<Ranking>): Promise<Ballot> =
//            handleException {
//                val user = assertCredentialsValid(credentials)
//                val voter = findUserByName(voterName)
//                assertUserIsVoter(user, voter)
//                assertVoterIsEligibleToVoteInElection(voterName, electionName)
//                val existingBallotAndIndex = searchBallotAndIndex(electionName, voterName)
//                val now = clock.now()
//                val election = findElectionByName(electionName)
//                val ballot = Ballot(electionName, voterName, now, election.isActiveAsOf(now), rankings.normalize())
//                if (existingBallotAndIndex == null) {
//                    allBallots.add(ballot)
//                } else {
//                    val (_, index) = existingBallotAndIndex
//                    allBallots[index] = ballot
//                }
//                ballot
//            }
//
//    override fun setEndDate(credentials: Credentials, electionName: String, isoEndDate: String?): Promise<Election> =
//            handleException {
//                updateElection(credentials, electionName) { election ->
//                    val date = if (isoEndDate == null) null else Date(isoEndDate)
//                    election.copy(end = date)
//                }
//            }
//
//    override fun setSecretBallot(credentials: Credentials, electionName: String, secretBallot: Boolean): Promise<Election> =
//            handleException {
//                updateElection(credentials, electionName) { election ->
//                    election.copy(secretBallot = secretBallot)
//                }
//            }
//
//    private fun updateElection(credentials: Credentials, electionName: String, update: (Election) -> Election): Election {
//        val (election, index) = findElectionAndIndexByName(electionName)
//        val user = assertCredentialsValid(credentials)
//        assertUserOwnsElection(user, election)
//        val updatedElection = update(election)
//        elections[index] = updatedElection
//        return updatedElection
//    }
//
//    private fun searchUser(nameOrEmail: String): User? =
//            users.find { user -> user.name == nameOrEmail || user.email == nameOrEmail }
//
//    private fun findUserByName(name: String): User =
//            searchUserByName(name) ?: throw RuntimeException("User with name '$name' not found")
//
//    private fun searchUserByName(name: String): User? = users.find { user -> user.name == name }
//
//    private fun findElectionByName(electionName: String): Election =
//            searchElectionByName(electionName) ?: throw RuntimeException("Election with name '$electionName' not found")
//
//    private fun searchElectionByName(electionName: String): Election? =
//            searchElectionAndIndexByName(electionName)?.election
//
//    private fun searchElectionAndIndexByName(electionName: String): ElectionAndIndex? {
//        val index = elections.indexOfFirst { election -> election.name == electionName }
//        return if (index == -1) null else ElectionAndIndex(elections[index], index)
//    }
//
//    private fun findElectionAndIndexByName(electionName: String): ElectionAndIndex =
//            searchElectionAndIndexByName(electionName)
//                    ?: throw RuntimeException("Election with name '$electionName' not found")
//
//    private fun searchBallotAndIndex(electionName: String, voterName: String): BallotAndIndex? {
//        val index = allBallots.indexOfFirst { ballot ->
//            ballot.electionName == electionName && ballot.voterName == voterName
//        }
//        return if (index == -1) null else BallotAndIndex(allBallots[index], index)
//    }
//
//    private fun searchBallot(electionName: String, voterName: String): Ballot? =
//            searchBallotAndIndex(electionName, voterName)?.ballot
//
//    private fun assertUserNameDoesNotExist(name: String) {
//        if (users.find { it.name == name } != null) {
//            throw RuntimeException("User named '$name' already exists")
//        }
//    }
//
//    private fun assertUserEmailDoesNotExist(email: String) {
//        if (users.find { it.email == email } != null) {
//            throw RuntimeException("User with email '$email' already exists")
//        }
//    }
//
//    private fun assertElectionNameDoesNotExist(name: String) {
//        if (elections.find { it.name == name } != null) {
//            throw RuntimeException("Election named '$name' already exists")
//        }
//    }
//
//    private fun assertUserIsVoter(user: User, voter: User) {
//        if (user.name != voter.name) {
//            throw RuntimeException("User '${user.name}' is not allowed view details for '${voter.name}'")
//        }
//    }
//
//    private fun assertVoterIsEligibleToVoteInElection(voterName: String, electionName: String) {
//        val eligibleVoters = votersByElection.getValue(electionName)
//        if (!eligibleVoters.contains(voterName)) {
//            throw RuntimeException("Voter '$voterName' is not eligible to vote in election '$electionName'")
//        }
//    }
//
//    private fun assertUserOwnsElection(user: User, election: Election) {
//        if (user.name != election.ownerName) {
//            throw RuntimeException("User '${user.name}' is not allowed to edit election '${election.name}' owned by ${election.ownerName}")
//        }
//    }
//
//    private fun assertCredentialsValid(credentials: Credentials): User {
//        val user = findUserByName(credentials.name)
//        if (credentials.password != user.password) {
//            throw RuntimeException("Incorrect password for user ${credentials.name}")
//        }
//        return user
//    }
//
//    private fun createUser(name: String, email: String, password: String): User {
//        val user = User(name, email, password)
//        users.add(user)
//        return user
//    }
//
//    private fun createElection(ownerName: String, electionName: String): Election {
//        val election = Election(ownerName, electionName)
//        elections.add(election)
//        candidatesByElection[electionName] = emptyList()
//        votersByElection[electionName] = emptyList()
//        return election
//    }
//
//    private fun electionsEligibleForVoter(voterName: String): List<String> =
//            votersByElection.filterValues { voters ->
//                voters.contains(voterName)
//            }.map { it.key }
//
//    private fun ballotForElectionAndVoter(electionName: String, voterName: String): Ballot =
//            searchBallot(electionName, voterName) ?: createEmptyBallot(electionName, voterName)
//
//    private fun createEmptyBallot(electionName: String, voterName: String): Ballot {
//        val election = findElectionByName(electionName)
//        val whenCast = null
//        val isActive = election.end == null || clock.now().getTime() < election.end.getTime()
//        val rankings = emptyList<Ranking>()
//        val ballot = Ballot(
//                election.name,
//                voterName,
//                whenCast,
//                isActive,
//                rankings)
//        return ballot
//    }
//
    private fun Db.Election.toApi():Election =
        Election(
                ownerName = owner,
                name = name,
                end = end?.toDate(),
                secretBallot = secret,
                status = status.toApiStatus(),
                candidateCount = 0,
                voterCount = 0
        )

    private fun Db.Status.toApiStatus():Election.ElectionStatus =
        when(this){
            Db.Status.EDITING -> Election.ElectionStatus.EDITING
            Db.Status.LIVE -> Election.ElectionStatus.LIVE
            Db.Status.COMPLETE -> Election.ElectionStatus.CONCLUDED
        }
//
//    private fun setVoters(user: User, electionName: String, eligibleVoters: List<String>) {
//                val election = findElectionByName(electionName)
//                assertUserOwnsElection(user, election)
//                val cleanVoters = eligibleVoters.clean()
//                val existingVoters = db.voter.listWhere { it.electionName == electionName }
//                db.voter.removeAllByValues(existingVoters)
//                val newVoters = cleanVoters.map { Db.Voter(it, electionName) }
//                db.voter.add(newVoters)
//                updateElection(credentials, electionName) { it.copy(voterCount = cleanVoters.size) }
//                val isAllVoters = cleanVoters.size == db.user.countAll()
//                Voters(cleanVoters, isAllVoters)
//            }

    private fun String.toDate():Date = Date(this)

    private fun searchUserByNameOrEmail(nameOrEmail:String):Db.User? {
        val users = db.user.listWhere { it.name == nameOrEmail || it.email == nameOrEmail }
        return users.nullOrOne()
    }

    private fun assertUserNameDoesNotExist(name:String){
        if(db.user.existsWhere{it.name == name}){
            throw RuntimeException("User with name '$name' already exists")
        }
    }
    private fun assertUserEmailDoesNotExist(email:String){
        if(db.user.existsWhere{it.email == email}){
            throw RuntimeException("User with email '$email' already exists")
        }
    }

    private fun assertCredentialsValid(credentials: Credentials){
        val user = db.user.find(credentials.name)
        if(user.password != credentials.password){
            throw RuntimeException("Unable to authenticate user '${credentials.name}'")
        }
    }
    private fun assertElectionNameDoesNotExist(electionName:String){
        if(db.election.keyExists(electionName)){
            throw RuntimeException("Election named '$electionName' already exists")
        }
    }

    private fun <T> List<T>.exactlyOne():T  =
            when(size){
                1 -> get(0)
                else ->throw RuntimeException("Exactly 1 element expected, got $size")
            }

    private fun <T> List<T>.nullOrOne():T?  =
            when(size){
                0 -> null
                1 -> get(0)
                else -> throw RuntimeException("0 or 1 elements expected, got $size")
            }

    private fun <T> handleException(f: () -> T): Promise<T> =
            try {
                Promise.resolve(f())
            } catch (ex: RuntimeException) {
                Promise.reject(ex)
            }

    private fun copyDbElection(newElectionName:String, electionToCopy:Db.Election):Db.Election{
                        val newDbElection =Db.Election(
                        owner = electionToCopy.owner,
                        name = newElectionName,
                        end = null,
                        secret = electionToCopy.secret,
                        status = Db.Status.EDITING)
        db.election.add(newDbElection)
        return newDbElection
//                val oldDbCandidates = db.candidate.listWhere{ it.electionName == electionToCopyName }
//                val newDbCandidates = oldDbCandidates.map{ it.copy(electionName = electionToCopyName)}
//                val oldDbVoters = db.voter.listWhere { it.electionName == electionToCopyName }
//                val newDbVoters = oldDbVoters.map { it.copy(electionName = electionToCopyName)}
//                db.election.add(newDbElection)
//                db.candidate.add(newDbCandidates)
//                db.voter.add(newDbVoters)
//                val apiElection = Election(
//                        ownerName = user.name,
//                        name = cleanNewElectionName,
//                        end = electionToCopy.end,
//                        secretBallot = electionToCopy.secretBallot,
//                        status = Election.ElectionStatus.EDITING,
//                        candidateCount = electionToCopy.candidateCount,
//                        voterCount = electionToCopy.voterCount)
//                apiElection

    }

    private fun copyDbElectionCandidates(newElectionName: String)
}
