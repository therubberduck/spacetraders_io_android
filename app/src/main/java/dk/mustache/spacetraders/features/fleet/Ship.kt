package dk.mustache.spacetraders.features.fleet

data class Ship(
    val symbol: String,
    val systemSymbol: String,
    val waypointSymbol: String,
    val status: ShipStatus,
    val flightMode: FlightMode,
    val cargo: Int,
    val fuel: Int,
    val components: List<ShipComponent>,
    val combinedRequirements: ShipRequirements
)

data class ShipComponent
(
    val symbol: String,
    val name: String,
    val description: String,
    val type: ShipComponentType,
    val stats: ShipComponentStats,
    val requirements: ShipRequirements
)

interface ShipComponentStats {
    fun notEmpty(): Boolean
}

data class ShipModuleStats(
    val capacity: Int? = null,
    val range: Int? = null
): ShipComponentStats {
    override fun notEmpty(): Boolean {
        return capacity != null || range != null
    }
}

data class ShipMountStats(
    val strength: Int? = null,
    val deposits: List<String>? = null
): ShipComponentStats {
    override fun notEmpty(): Boolean {
        return strength != null || !deposits.isNullOrEmpty()
    }
}

enum class ShipComponentType {
    ENGINE,
    REACTOR,
    MODULE,
    MOUNT
}

data class ShipRequirements(
    val power: Int,
    val crew: Int,
    val slots: Int
)