package pages

import model.Ballot
import model.Election

interface Page {
    val name: String
    fun unsupported(transitionName: String): Page = navError("Unsupported transition $transitionName from page $name")
    fun navLogin(): Page = unsupported("navLogin")
    fun navRegister(): Page = unsupported("navRegister")
    fun navHome(): Page = unsupported("navHome")
    fun navPrototype(): Page = unsupported("navPrototype")
    fun navError(message: String): Page = UnexpectedErrorPage(message)

    companion object {
        val initial = LoginPage(errorMessage = null)
    }
}

data class LoginPage(val errorMessage: String?) : Page {
    override val name: String = "login"
    override fun navRegister(): Page = RegisterPage(errorMessage = null)
}

data class RegisterPage(val errorMessage: String?) : Page {
    override val name: String = "register"
    override fun navLogin(): Page = LoginPage(errorMessage = null)
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
    override fun navLogin(): Page = LoginPage(errorMessage = null)
    override fun navRegister(): Page = RegisterPage(errorMessage = null)
    override fun navPrototype(): Page = PrototypePage
}
