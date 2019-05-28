package db

class InMemoryDb : Db {
    override val user: Table<String, Db.User> = InMemoryTable("user")
    override val election: Table<String, Db.Election> = InMemoryTable("election")
    override val candidate: Table<Db.Candidate, Db.Candidate> = InMemoryTable("candidate")
    override val voter: Table<Db.Voter, Db.Voter> = InMemoryTable("voter")
    override val ballot: Table<Db.Voter, Db.Ballot> = InMemoryTable("ballot")
    override val ranking: Table<Db.Ranking.RankingPrimaryKey, Db.Ranking> = InMemoryTable("ranking")
    override val tally: Table<Db.Tally.TallyPrimaryKey, Db.Tally> = InMemoryTable("tally")
}
