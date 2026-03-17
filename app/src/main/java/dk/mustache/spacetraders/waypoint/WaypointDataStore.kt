package dk.mustache.spacetraders.waypoint

import dk.mustache.spacetraders.api.DataResult
import dk.mustache.spacetraders.common.WaypointId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaypointDataStore @Inject constructor(
    private val waypointRepository: WaypointRepository
) {
    private val waypoints: MutableMap<WaypointId, Waypoint> = mutableMapOf()

    private val _allWaypoints = MutableStateFlow<List<Waypoint>>(emptyList())
    val allWaypoints = _allWaypoints.asStateFlow()

    suspend fun fetchWaypoint(waypointId: WaypointId): DataResult<Waypoint> {
        val result = waypointRepository.getWaypoint(waypointId)
        if(result is DataResult.Success) {
            waypoints[waypointId] = result.data
            _allWaypoints.value = waypoints.values.toList()
        }
        return result
    }

    suspend fun getWaypoint(waypointId: WaypointId): DataResult<Waypoint> {
        val cachedWaypoint = waypoints[waypointId]
        if(cachedWaypoint != null) {
            return DataResult.Success(cachedWaypoint)
        }
        return fetchWaypoint(waypointId)
    }
}