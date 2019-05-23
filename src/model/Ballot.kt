package model

data class Ballot(val electionName: String,
                  val voterName: String,
                  val rankings: List<Ranking>) {
    enum class Action(val displayName: String) {
        CAST("cast"), EDIT("edit"), VIEW("view")
    }
}
