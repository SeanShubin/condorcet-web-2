package model

data class Ranking(val rank: Int?, val candidateName: String) {
    companion object {
        fun List<Ranking>.normalize(): List<Ranking> {
            val ranks: List<Int> = this.mapNotNull { it.rank }.sorted().distinct()
            val normalizedRanks: List<Int?> = (1..ranks.size).toList()
            val mappings = ranks.zip(normalizedRanks).toMap()
            return this.map {
                it.copy(rank = mappings[it.rank])
            }
        }
    }
}
