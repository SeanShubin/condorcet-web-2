package model

data class Ballot(val electionName: String, val voterName: String, val rankings: List<Ranking>)
