package dk.mustache.spacetraders.agent

data class ApiAgent(
    val accountId: String?,
    val symbol: String?,
    val headquarters: String?,
    val credits: Int?,
    val startingFaction: String?,
    val shipCount: Int?,
)