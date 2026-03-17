package dk.mustache.spacetraders.api

import dk.mustache.spacetraders.agent.ApiAgent
import dk.mustache.spacetraders.contracts.ApiContract
import dk.mustache.spacetraders.waypoint.Waypoint
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @GET("my/agent")
    suspend fun getMyAgent(): Response<ApiCall<ApiAgent>>

    @GET("my/contracts")
    suspend fun getMyContracts(): Response<ApiCall<List<ApiContract>>>

    @POST("my/contracts/{contractId}/accept")
    suspend fun acceptContract(@Path("contractId") contractId: String): Response<ApiCall<Unit>>

    @GET("systems/{system}/waypoints/{waypoint}")
    suspend fun getWaypoint(
        @Path("system") system: String,
        @Path("waypoint") waypoint: String
    ): Response<ApiCall<Waypoint>>
}