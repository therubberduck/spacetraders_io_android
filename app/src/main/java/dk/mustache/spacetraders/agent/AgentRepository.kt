package dk.mustache.spacetraders.agent

import dk.mustache.spacetraders.api.ApiCall
import dk.mustache.spacetraders.api.DataResult
import dk.mustache.spacetraders.api.RetrofitInstance
import dk.mustache.spacetraders.api.richCall
import dk.mustache.spacetraders.common.WaypointId
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgentRepository @Inject constructor() {
    private val api = RetrofitInstance.retrofit.create(AgentApi::class.java)

    suspend fun getMyAgent(): DataResult<Agent> {
        return richCall(request = { api.getMyAgent() }, conversion = { apiAgent ->
            Agent(
                accountId = apiAgent.accountId
                    ?: throw NullPointerException("Agent accountId is null"),
                symbol = apiAgent.symbol ?: throw NullPointerException("Agent symbol is null"),
                headquarters = WaypointId.fromString(
                    apiAgent.headquarters
                        ?: throw NullPointerException("Agent headquarters is null")
                ),
                credits = apiAgent.credits ?: throw NullPointerException("Agent credits is null"),
                startingFaction = apiAgent.startingFaction
                    ?: throw NullPointerException("Agent startingFaction is null"),
                shipCount = apiAgent.shipCount
                    ?: throw NullPointerException("Agent shipCount is null")
            )
        })
    }
}

private interface AgentApi {
    @GET("my/agent")
    suspend fun getMyAgent(): Response<ApiCall<ApiAgent>>
}