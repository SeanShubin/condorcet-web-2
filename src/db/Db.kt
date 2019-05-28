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
                    val password: String) : TableRow<String> {
        override val primaryKey: String get() = name
        override val cells: List<Any?> = listOf(name, email, password)
    }

    data class Election(val owner: String,
                        val name: String,
                        val end: String?,
                        val secret: Boolean,
                        val status: Status) : TableRow<String> {
        override val primaryKey: String get() = name
        override val cells: List<Any?> = listOf(owner, name, end, secret, status)
    }

    data class Candidate(val name: String,
                         val electionName: String) : TableRow<Candidate> {
        override val primaryKey: Candidate get() = this
        override val cells: List<Any?> = listOf(name, electionName)
    }

    data class Voter(val userName: String,
                     val electionName: String) : TableRow<Voter> {
        override val primaryKey: Voter get() = this
        override val cells: List<Any?> = listOf(userName, electionName)
    }

    data class Ballot(val voterName: String,
                      val electionName: String,
                      val whenCast: String?) : TableRow<Voter> {
        override val primaryKey: Voter get() = Voter(voterName, electionName)
        override val cells: List<Any?> = listOf(voterName, electionName, whenCast)
    }

    data class Ranking(val voterName: String,
                       val electionName: String,
                       val candidateName: String,
                       val rank: Int) : TableRow<Ranking.RankingPrimaryKey> {
        data class RankingPrimaryKey(val userName: String,
                                     val electionName: String,
                                     val candidateName: String)

        override val primaryKey: RankingPrimaryKey = RankingPrimaryKey(voterName, electionName, candidateName)
        override val cells: List<Any?> = listOf(voterName, electionName, candidateName, rank)
    }

    data class Tally(val electionName: String,
                     val candidateName: String,
                     val rank: Int) : TableRow<Tally.TallyPrimaryKey> {
        data class TallyPrimaryKey(val electionName: String,
                                   val candidateName: String)

        override val primaryKey: TallyPrimaryKey get() = TallyPrimaryKey(electionName, candidateName)
        override val cells: List<Any?> = listOf(electionName, candidateName, rank)
    }
}