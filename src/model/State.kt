package model

data class State(val pageName: String,
                 val ballotPage: Ballot? = null,
                 val ballotsPage: VoterAndBallots? = null,
                 val candidatesPage: ElectionAndCandidates? = null,
                 val electionPage: Election? = null,
                 val electionsPage: List<Election>? = null,
                 val votersPage: ElectionAndVoters? = null,
                 val errorPage: String? = null) {
    fun navLogin(): State = copy(pageName = "login")
    fun navRegister(): State = copy(pageName = "register")
    fun navHome(): State = copy(pageName = "home")
    fun navPrototype(): State = copy(pageName = "prototype")
    fun error(message: String): State = copy(pageName = "error", errorPage = message)
    companion object{
        val initial:State = State(pageName = "login")
    }
}
