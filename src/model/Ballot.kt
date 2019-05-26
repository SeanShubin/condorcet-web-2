package model

import kotlin.js.Date

data class Ballot(val electionName: String,
                  val voterName: String,
                  val whenCast: Date?,
                  val isActive: Boolean,
                  val rankings: List<Ranking>)
