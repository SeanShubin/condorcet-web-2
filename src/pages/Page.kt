package pages

import model.Ballot
import model.Election

interface Page {
    val name: String
    fun unsupported(transitionName: String): Page =
            navError("Unsupported transition $transitionName from page $name")

    fun navLogin(): Page = LoginPage(errorMessage = null)
    fun navRegister(): Page = RegisterPage(errorMessage = null)
    fun navHome(): Page = HomePage
    fun navPrototype(): Page = PrototypePage
    fun navError(message: String): Page = UnexpectedErrorPage(message)
    fun loginFailure(message: String): Page = LoginPage(errorMessage = message)

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

object HomePage : Page {
    override val name: String = "home"
}

data class BallotPage(val ballot: Ballot) : Page {
    override val name: String = "ballot"
}

data class BallotsPage(val voterName: String, val ballots: List<Ballot>) : Page {
    override val name: String = "ballots"
}

data class CandidatesPage(val electionName: String, val candidates: List<String>) : Page {
    override val name: String = "candidates"
}

data class ElectionPage(val election: Election) : Page {
    override val name: String = "election"
}

data class ElectionsPage(val elections: List<Election>) : Page {
    override val name: String = "elections"
}

data class VotersPage(val electionName: String, val voters: List<String>) : Page {
    override val name: String = "voters"
}

object PrototypePage : Page {
    override val name: String = "prototype"
}

data class UnexpectedErrorPage(val message: String) : Page {
    override val name: String = "unexpected"
}
