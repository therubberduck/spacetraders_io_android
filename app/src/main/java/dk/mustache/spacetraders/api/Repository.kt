package dk.mustache.spacetraders.api

import dk.mustache.spacetraders.agent.Agent
import dk.mustache.spacetraders.common.WaypointId
import dk.mustache.spacetraders.contracts.Contract
import dk.mustache.spacetraders.mdate.MDate
import dk.mustache.spacetraders.waypoint.Waypoint
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor() {
    private val api = RetrofitInstance.apiInterface

    suspend fun getMyAgent(): NetworkResult<Agent> {
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

    suspend fun getMyContracts(): NetworkResult<List<Contract>> {
        return richCall(request = { api.getMyContracts() }, conversion = { apiContracts ->
            apiContracts.map {
                Contract(
                    id = it.id ?: throw NullPointerException("Contract id is null"),
                    factionSymbol = it.factionSymbol
                        ?: throw NullPointerException("Contract factionSymbol is null"),
                    type = it.type ?: throw NullPointerException("Contract type is null"),
                    deadline = MDate.fromTimestamp(
                        it.terms?.deadline ?: throw NullPointerException(
                            "Contract deadline is null"
                        )
                    ),
                    paymentOnAccepted = it.terms.payment?.onAccepted
                        ?: throw NullPointerException("Contract paymentOnAccepted is null"),
                    paymentOnFulfilled = it.terms.payment.onFulfilled ?: throw NullPointerException(
                        "Contract paymentOnFulfilled is null"
                    ),
                    deliver = it.terms.deliver?.mapNotNull {
                        it
                    } ?: throw NullPointerException("Contract deliver is null"),
                    accepted = it.accepted
                        ?: throw NullPointerException("Contract accepted is null"),
                    fulfilled = it.fulfilled
                        ?: throw NullPointerException("Contract fulfilled is null"),
                    expiration = MDate.fromTimestamp(
                        it.expiration ?: throw NullPointerException("Contract expiration is null")
                    ),
                    deadlineToAccept = it.deadlineToAccept?.let { MDate.fromTimestamp(it) }
                )
            }
        })
    }

    suspend fun acceptContract(contractId: String): NetworkResult<Unit> {
        return richCall(request = {
            api.acceptContract(contractId)
        })
    }

    suspend fun getWaypoint(waypointId: WaypointId): NetworkResult<Waypoint> {
        return richCall(request = { api.getWaypoint(waypointId.systemId, waypointId.waypointId) })
    }

    private suspend inline fun <T, reified U> richCall(
        request: suspend () -> Response<ApiCall<T>>,
        noinline conversion: ((T) -> U)? = null
    ): NetworkResult<U> {
        return try {
            val response = request()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (conversion != null)
                        try {
                            NetworkResult.Success(conversion(body.data))
                        } catch (e: Exception) {
                            NetworkResult.Error("Conversion failed: ${e.message}")
                        }
                    else if (body.data is U)
                        NetworkResult.Success(body.data as U)
                    else
                        NetworkResult.Error("Conversion failed")
                } else {
                    NetworkResult.Error("Response body is null")
                }
            } else {
                NetworkResult.Error("Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Unknown error occurred")
        }
    }
}

sealed class NetworkResult<T>(val isSuccess: Boolean) {
    data class Success<T>(val data: T) : NetworkResult<T>(isSuccess = true)
    data class Error<T>(val message: String) : NetworkResult<T>(isSuccess = false)
}