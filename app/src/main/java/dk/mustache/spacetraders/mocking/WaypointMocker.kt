package dk.mustache.spacetraders.mocking

import dk.mustache.spacetraders.common.dataclasses.SimpleSymbol
import dk.mustache.spacetraders.common.dataclasses.Trait
import dk.mustache.spacetraders.features.waypoint.Chart
import dk.mustache.spacetraders.features.waypoint.Waypoint

object WaypointMocker {
    fun one(
        symbol: String = "X1-HF93-A1",
        type: String = "PLANET",
        systemSymbol: String = "X1-HF93",
        x: Int = 1,
        y: Int = 2,
        orbitals: List<SimpleSymbol> = listOf(),
        traits: List<Trait> = TraitMocker.standardSet(),
        isUnderConstruction: Boolean = false,
        faction: SimpleSymbol = SimpleSymbol("COSMIC"),
        modifiers: List<String> = listOf(),
        chart: Chart = Chart(
            waypointSymbol = "X1-HF93-A1",
            submittedBy = "TestUser",
            submittedOn = "2026-03-15T13:00:40.752Z"
        )
    ): Waypoint {
        return Waypoint(
            symbol = symbol,
            type = type,
            systemSymbol = systemSymbol,
            x = x,
            y = y,
            orbitals = orbitals,
            traits = traits,
            isUnderConstruction = isUnderConstruction,
            faction = faction,
            modifiers = modifiers,
            chart = chart
        )
    }
}