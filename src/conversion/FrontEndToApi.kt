package conversion

import model.Ballot
import model.Ranking
import pages.BallotPage

object FrontEndToApi {
    fun BallotPage.toApi(): Ballot =
            Ballot(electionName, voterName, whenCast, isActive, rankings.map { it.toApi() })

    fun BallotPage.Ranking.toApi(): Ranking = Ranking(rank.toIntOrNull(), candidateName)
}
