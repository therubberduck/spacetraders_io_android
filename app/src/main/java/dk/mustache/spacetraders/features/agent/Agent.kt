package dk.mustache.spacetraders.features.agent

import dk.mustache.spacetraders.common.dataclasses.WaypointId

data class Agent(
    val accountId: String,
    val symbol: String,
    val headquarters: WaypointId,
    val credits: Int,
    val startingFaction: String,
    val shipCount: Int,
    val isLoaded: Boolean = true
) {
    companion object {
        val EMPTY = Agent(
            accountId = "",
            symbol = "",
            headquarters = WaypointId("", "", ""),
            credits = 0,
            startingFaction = "",
            shipCount = 0,
            isLoaded = false
        )
    }
}