package dk.mustache.spacetraders.features.fleet

import dk.mustache.spacetraders.api.helpers.ApiCall
import dk.mustache.spacetraders.api.helpers.DataResult
import dk.mustache.spacetraders.api.RetrofitInstance
import dk.mustache.spacetraders.api.helpers.richCall
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FleetRepository @Inject constructor() {
    private val api = RetrofitInstance.retrofit.create(FleetApi::class.java)

    suspend fun fetchMyShips(): DataResult<List<Ship>> {
        return richCall({
            api.getMyShips()
        }, { resultData ->
            resultData.map { apiShip ->
                convertShip(apiShip)
            }
        })
    }

    private fun convertShip(apiShip: ApiShip): Ship {
        val powerRequirements =
            (apiShip.modules?.sumOf { it.requirements?.power ?: 0 } ?: 0) +
                    (apiShip.mounts?.sumOf { it.requirements?.power ?: 0 } ?: 0)
        val crewRequirements =
            (apiShip.modules?.sumOf { it.requirements?.crew ?: 0 } ?: 0) +
                    (apiShip.mounts?.sumOf { it.requirements?.crew ?: 0 } ?: 0)

        val modules = apiShip.modules?.map { moduleData ->
            ShipComponent(
                symbol = moduleData.symbol ?: throw NullPointerException("Module symbol for ${apiShip.symbol} is null"),
                name = moduleData.name ?: throw NullPointerException("Module name for ${apiShip.symbol} is null"),
                description = moduleData.description ?: "",
                type = ShipComponentType.MODULE,
                stats = ShipModuleStats(
                    capacity = moduleData.capacity,
                    range = moduleData.range
                ),
                requirements = ShipRequirements(
                    power = moduleData.requirements?.power ?: 0,
                    crew = moduleData.requirements?.crew ?: 0,
                    slots = moduleData.requirements?.slots ?: 0
                )
            )
        } ?: emptyList()
        val mounts = apiShip.mounts?.map { mountData ->
            ShipComponent(
                symbol = mountData.symbol ?: throw NullPointerException("Mount symbol for ${apiShip.symbol} is null"),
                name = mountData.name ?: throw NullPointerException("Mount name for ${apiShip.symbol} is null"),
                description = mountData.description ?: "",
                type = ShipComponentType.MOUNT,
                stats = ShipMountStats(
                    strength = mountData.strength,
                    deposits = mountData.deposits
                ),
                requirements = ShipRequirements(
                    power = mountData.requirements?.power ?: 0,
                    crew = mountData.requirements?.crew ?: 0,
                    slots = mountData.requirements?.slots ?: 0
                )
            )
        } ?: emptyList()

        return Ship(
            symbol = apiShip.symbol ?: throw NullPointerException("Ship symbol is null"),
            systemSymbol = apiShip.nav?.systemSymbol ?: throw NullPointerException("Ship systemSymbol is null"),
            waypointSymbol = apiShip.nav.waypointSymbol ?: throw NullPointerException("Ship waypointSymbol is null"),
            status = apiShip.nav.status?.let {
                when (it) {
                    "IN_TRANSIT" -> ShipStatus.IN_TRANSIT
                    "IN_ORBIT" -> ShipStatus.IN_ORBIT
                    "DOCKED" -> ShipStatus.DOCKED
                    else -> throw IllegalArgumentException("Unknown ship status: $it")
                }
            } ?: throw NullPointerException("Ship status is null"),
            flightMode = apiShip.nav.flightMode?.let {
                when (it) {
                    "DRIFT" -> FlightMode.DRIFT
                    "STEALTH" -> FlightMode.STEALTH
                    "CRUISE" -> FlightMode.CRUISE
                    "BURN" -> FlightMode.BURN
                    else -> throw IllegalArgumentException("Unknown ship flightMode: $it")
                }
            } ?: throw NullPointerException("Ship flightMode is null"),
            cargo = apiShip.cargo?.units ?: 0,
            fuel = apiShip.fuel?.current ?: 0,
            components = modules + mounts,
            combinedRequirements = ShipRequirements(powerRequirements, crewRequirements, 0)
        )
    }
}

private interface FleetApi {

    @GET("my/ships")
    suspend fun getMyShips(): Response<ApiCall<List<ApiShip>>>

}