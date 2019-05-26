package db

class MemDb : Db {
    override val user: Table<String, Db.User> = MemTable("user")
    override val election: Table<String, Db.Election> = MemTable("election")
    override val candidate: Table<Db.Candidate, Db.Candidate> = MemTable("candidate")
    override val voter: Table<Db.Voter, Db.Voter> = MemTable("voter")
    override val ballot: Table<Db.Voter, Db.Ballot> = MemTable("ballot")
    override val ranking: Table<Db.Ranking.RankingPrimaryKey, Db.Ranking> = MemTable("ranking")
    override val tally: Table<Db.Tally.TallyPrimaryKey, Db.Tally> = MemTable("tally")
}
