package pages

import model.Ballot
import model.Credentials
import model.Election
import model.Election.ElectionStatus

interface Page {
    val name: String
    fun unsupported(transitionName: String): Page =
            navError("Unsupported transition $transitionName from page $name")

    fun navLogin(): Page = LoginPage(errorMessage = null)
    fun navRegister(): Page = RegisterPage(errorMessage = null)
    fun navHome(credentials: Credentials): Page = HomePage(credentials)
    fun navPrototype(): Page = PrototypePage
    fun navError(message: String): Page = UnexpectedErrorPage(message)
    fun loginFailure(message: String): Page = LoginPage(errorMessage = message)
    fun registerFailure(message: String): Page = RegisterPage(errorMessage = message)
    fun navElections(credentials: Credentials, elections: List<Election>): Page = ElectionsPage(credentials, elections)
    fun navElection(credentials: Credentials, election: Election, errorMessage: String? = null): Page =
            ElectionPage.create(credentials, election, errorMessage)
    fun navBallots(credentials: Credentials,
                   voterName: String,
                   ballots: List<Ballot>): Page = BallotsPage(credentials, voterName, ballots)

    fun startChanged(start: String): Page = unsupported("startChanged")
    fun endChanged(end: String): Page = unsupported("endChanged")
    fun secretBallotChanged(secretBallot: Boolean): Page = unsupported("secretBallotChanged")
    fun navCandidates(credentials: Credentials, electionName: String, candidates: List<String>): Page = CandidatesPage(credentials, electionName, candidates)
    fun navVoters(credentials: Credentials, electionName: String, voters: List<String>): Page = VotersPage(credentials, electionName, voters)

    companion object {
        val initial = LoginPage(errorMessage = null)
    }
}

data class LoginPage(val errorMessage: String?) : Page {
    override val name: String = "login"
}

data class RegisterPage(val errorMessage: String?) : Page {
    override val name: String = "register"
}

data class HomePage(val credentials: Credentials) : Page {
    override val name: String = "home"
}

data class BallotPage(val credentials: Credentials, val ballot: Ballot) : Page {
    override val name: String = "ballot"
}

data class BallotsPage(val credentials: Credentials,
                       val voterName: String,
                       val ballots: List<Ballot>) : Page {
    override val name: String = "ballots"
}

data class CandidatesPage(val credentials: Credentials,
                          val electionName: String,
                          val candidates: List<String>) : Page {
    override val name: String = "candidates"
}

data class ElectionPage(val credentials: Credentials,
                        val electionName: String,
                        val ownerName: String,
                        val status: ElectionStatus,
                        val start: String,
                        val end: String,
                        val secretBallot: Boolean,
                        val candidateCount: Int,
                        val voterCount: Int,
                        val errorMessage: String?) : Page {
    override val name: String = "election"
    override fun startChanged(start: String): Page = copy(start = start)
    override fun endChanged(end: String): Page = copy(end = end)
    override fun secretBallotChanged(secretBallot: Boolean): Page = copy(secretBallot = secretBallot)

    companion object {
        fun create(credentials: Credentials, election: Election, errorMessage: String? = null): ElectionPage =
                ElectionPage(
                        credentials,
                        election.name,
                        election.ownerName,
                        election.status,
                        election.startString,
                        election.endString,
                        election.secretBallot,
                        election.candidateCount,
                        election.voterCount,
                        errorMessage)

    }
}

data class ElectionsPage(val credentials: Credentials, val elections: List<Election>) : Page {
    override val name: String = "elections"
}

data class VotersPage(val credentials: Credentials,
                      val electionName: String,
                      val voters: List<String>) : Page {
    override val name: String = "voters"
}

object PrototypePage : Page {
    override val name: String = "prototype"
}

data class UnexpectedErrorPage(val message: String) : Page {
    override val name: String = "unexpected"
}
