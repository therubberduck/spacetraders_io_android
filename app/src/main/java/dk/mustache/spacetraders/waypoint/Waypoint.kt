package dk.mustache.spacetraders.waypoint

import dk.mustache.spacetraders.common.SimpleSymbol
import dk.mustache.spacetraders.common.Trait

data class Waypoint(
    val symbol: String,
    val type: String,
    val systemSymbol: String,
    val x: Int,
    val y: Int,
    val orbitals: List<SimpleSymbol>,
    val traits: List<Trait>,
    val isUnderConstruction: Boolean,
    val faction: SimpleSymbol,
    val modifiers: List<String>,
    val chart: Chart
) {
    companion object {
        val EMPTY = Waypoint(
            symbol = "",
            type = "",
            systemSymbol = "",
            x = 0,
            y = 0,
            orbitals = emptyList(),
            traits = emptyList(),
            isUnderConstruction = false,
            faction = SimpleSymbol(""),
            modifiers = emptyList(),
            chart = Chart("", "", ""),
        )
    }
}

data class Chart(
    val waypointSymbol: String,
    val submittedBy: String,
    val submittedOn: String
)
