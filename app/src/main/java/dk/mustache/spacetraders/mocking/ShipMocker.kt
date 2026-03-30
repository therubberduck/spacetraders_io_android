package dk.mustache.spacetraders.mocking

import dk.mustache.spacetraders.features.fleet.FlightMode
import dk.mustache.spacetraders.features.fleet.Ship
import dk.mustache.spacetraders.features.fleet.ShipComponent
import dk.mustache.spacetraders.features.fleet.ShipComponentType
import dk.mustache.spacetraders.features.fleet.ShipModuleStats
import dk.mustache.spacetraders.features.fleet.ShipMountStats
import dk.mustache.spacetraders.features.fleet.ShipRequirements
import dk.mustache.spacetraders.features.fleet.ShipStatus

object ShipMocker {
    fun one(
        symbol: String = "X1",
        cargo: Int = 40,
        fuel: Int = 89,
        components: List<ShipComponent> = modulesStandardSet(4) + mountStandardSet(3)
    ): Ship {
        val combinedRequirements = ShipRequirements(
            power = components.sumOf { it.requirements.power },
            crew = components.sumOf { it.requirements.crew },
            slots = 0,
        )

        return Ship(
            symbol = symbol,
            systemSymbol = "X1-HF93",
            waypointSymbol = "X1-HF93-A1",
            status = ShipStatus.IN_TRANSIT,
            flightMode = FlightMode.CRUISE,
            cargo = cargo,
            fuel = fuel,
            components = components,
            combinedRequirements = combinedRequirements
        )
    }

    fun standardSet(num: Int): List<Ship> {
        return (1..num).map {
            one(symbol = "X$it")
        }
    }

    fun oneShipModule(
        symbol: String = "MODULE_CARGO_HOLD_II",
        name: String = "Expanded Cargo Hold",
        description: String = "An expanded cargo hold module that provides more efficient storage space for a ship's cargo.",
        type: ShipComponentType = ShipComponentType.MODULE,
        stats: ShipModuleStats = ShipModuleStats(
            capacity = 40
        ),
        requirements: ShipRequirements = ShipRequirements(
            power = 5,
            crew = 3,
            slots = 2
        )
    ): ShipComponent {
        return ShipComponent(
            symbol = symbol,
            name = name,
            description = description,
            type = type,
            stats = stats,
            requirements = requirements,
        )
    }

    fun modulesStandardSet(num: Int): List<ShipComponent> {
        return (1..num).map {
            oneShipModule()
        }
    }

    fun oneShipMount(
        symbol: String = "MOUNT_SENSOR_ARRAY_II",
        name: String = "Sensor Array II",
        description: String = "An advanced sensor array that improves a ship's ability to detect and track other objects in space with greater accuracy and range.",
        type: ShipComponentType = ShipComponentType.MOUNT,
        stats: ShipMountStats = ShipMountStats(
            strength = 4
        ),
        requirements: ShipRequirements = ShipRequirements(
            power = 5,
            crew = 3,
            slots = 2
        )
    ): ShipComponent {
        return ShipComponent(
            symbol = symbol,
            name = name,
            description = description,
            type = type,
            stats = stats,
            requirements = requirements,
        )
    }

    fun mountStandardSet(num: Int): List<ShipComponent> {
        return (1..num).map {
            oneShipMount()
        }
    }
}