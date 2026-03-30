package dk.mustache.spacetraders.features.fleet

data class ApiShip(
    val symbol: String?,
    val nav: ApiNavigation?,
    val modules: List<ApiModule>?,
    val mounts: List<ApiMounts>?,
    val cargo: ApiCargo?,
    val fuel: ApiFuel?,
    val cooldown: ApiCooldown?,
)

data class ApiNavigation(
    val systemSymbol: String?,
    val waypointSymbol: String?,
    val route: ApiRoute?,
    val status: String?,
    val flightMode: String?,
)

data class ApiRoute(
    val destination: ApiDestination?,
    val origin: ApiDestination?,
    val departureTime: String?,
    val arrival: String?,
)

data class ApiDestination(
    val symbol: String?,
    val type: String?,
    val systemSymbol: String?,
    val x: Int?,
    val y: Int?,
)

data class ApiModule(
    val symbol: String?,
    val name: String?,
    val description: String?,
    val capacity: Int?,
    val range: Int?,
    val requirements: ApiRequirements?
)

data class ApiMounts(
    val symbol: String?,
    val name: String?,
    val description: String?,
    val strength: Int?,
    val deposits: List<String>?,
    val requirements: ApiRequirements?
)

data class ApiCargo(
    val capacity: Int?,
    val units: Int?,
    val inventory: List<ApiInventoryItem>?,
)

data class ApiInventoryItem(
    val symbol: String?,
    val name: String?,
    val description: String?,
    val units: Int?,
)

data class ApiFuel(
    val current: Int?,
    val capacity: Int?,
    val consumed: ApiFuelConsumed?,
)

data class ApiFuelConsumed(
    val amount: Int?,
    val timestamp: String?,
)

data class ApiCooldown(
    val shipSymbol: String?,
    val totalSeconds: Int?,
    val remainingSeconds: Int?,
    val expiration: String?
)

data class ApiRequirements(
    val power: Int?,
    val crew: Int?,
    val slots: Int?,
)