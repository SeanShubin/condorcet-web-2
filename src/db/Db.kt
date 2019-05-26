package db

interface Db {
    val user: Table<String, User>
    val election: Table<String, Election>
    val candidate: Table<Candidate, Candidate>
    val voter: Table<Voter, Voter>
    val ballot: Table<Voter, Ballot>
    val ranking: Table<Ranking.RankingPrimaryKey, Ranking>
    val tally: Table<Tally.TallyPrimaryKey, Tally>

    enum class Status {
        EDITING, LIVE, COMPLETE
    }

    data class User(val name: String,
                    val email: String,
                    val password: String) : HasPrimaryKey<String> {
        override val primaryKey: String get() = name
    }

    data class Election(val owner: String,
                        val name: String,
                        val end: String?,
                        val secret: Boolean,
                        val status: Status) : HasPrimaryKey<String> {
        override val primaryKey: String get() = name
    }

    data class Candidate(val name: String,
                         val electionName: String) : HasPrimaryKey<Candidate> {
        override val primaryKey: Candidate get() = this
    }

    data class Voter(val userName: String,
                     val electionName: String) : HasPrimaryKey<Voter> {
        override val primaryKey: Voter get() = this
    }

    data class Ballot(val userName: String,
                      val electionName: String,
                      val whenCast: String?) : HasPrimaryKey<Voter> {
        override val primaryKey: Voter get() = Voter(userName, electionName)
    }

    data class Ranking(val userName: String,
                       val electionName: String,
                       val candidateName: String,
                       val rank: Int) : HasPrimaryKey<Ranking.RankingPrimaryKey> {
        data class RankingPrimaryKey(val userName: String,
                                     val electionName: String,
                                     val candidateName: String)

        override val primaryKey: RankingPrimaryKey
            get() =
                RankingPrimaryKey(userName, electionName, candidateName)
    }

    data class Tally(val electionName: String,
                     val candidateName: String,
                     val rank: Int) : HasPrimaryKey<Tally.TallyPrimaryKey> {
        data class TallyPrimaryKey(val electionName: String,
                                   val candidateName: String)

        override val primaryKey: TallyPrimaryKey get() = TallyPrimaryKey(electionName, candidateName)
    }
}