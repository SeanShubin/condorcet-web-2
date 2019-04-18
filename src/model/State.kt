package model

data class State(val pageName: String,
                 val ballotPage: Ballot?,
                 val ballotsPage: VoterAndBallots?,
                 val candidatesPage: ElectionAndCandidates?,
                 val electionPage: Election?,
                 val electionsPage: List<Election>?,
                 val votersPage: ElectionAndVoters?,
                 val errorPage: String?) {
    fun navLogin(): State = copy(pageName = "login")
    fun navRegister(): State = copy(pageName = "register")
    fun navHome(): State = copy(pageName = "home")
    fun navPrototype(): State = copy(pageName = "prototype")
    fun error(message: String): State = copy(pageName = "error", errorPage = message)
}
