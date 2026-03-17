package dk.mustache.spacetraders.waypoint

import dk.mustache.spacetraders.api.ApiCall
import dk.mustache.spacetraders.api.DataResult
import dk.mustache.spacetraders.api.RetrofitInstance
import dk.mustache.spacetraders.api.richCall
import dk.mustache.spacetraders.common.WaypointId
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WaypointRepository @Inject constructor() {
    private val api = RetrofitInstance.retrofit.create(WaypointApi::class.java)

    suspend fun getWaypoint(waypointId: WaypointId): DataResult<Waypoint> {
        return richCall(request = { api.getWaypoint(waypointId.systemId, waypointId.waypointId) })
    }
}

private interface WaypointApi {
    @GET("systems/{system}/waypoints/{waypoint}")
    suspend fun getWaypoint(
        @Path("system") system: String,
        @Path("waypoint") waypoint: String
    ): Response<ApiCall<Waypoint>>
}