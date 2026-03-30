package dk.mustache.spacetraders.features.contracts

import dk.mustache.spacetraders.api.RetrofitInstance
import dk.mustache.spacetraders.api.helpers.ApiCall
import dk.mustache.spacetraders.api.helpers.DataResult
import dk.mustache.spacetraders.api.helpers.richCall
import dk.mustache.spacetraders.common.MDate
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContractRepository @Inject constructor() {
    private val api = RetrofitInstance.retrofit.create(ContractApi::class.java)

    suspend fun getMyContracts(): DataResult<List<Contract>> {
        return richCall(request = { api.getMyContracts() }, conversion = { apiContracts ->
            apiContracts.map {
                convertContract(it)
            }
        })
    }

    suspend fun acceptContract(contractId: String): DataResult<Contract> {
        return richCall(request = {
            api.acceptContract(contractId)
        }, conversion = { apiContract ->
            convertContract(apiContract)
        })
    }

    private fun convertContract(apiContract: ApiContract): Contract {
        return Contract(
            id = apiContract.id ?: throw NullPointerException("Contract id is null"),
            factionSymbol = apiContract.factionSymbol
                ?: throw NullPointerException("Contract factionSymbol is null"),
            type = apiContract.type ?: throw NullPointerException("Contract type is null"),
            deadline = MDate.fromTimestamp(
                apiContract.terms?.deadline ?: throw NullPointerException(
                    "Contract deadline is null"
                )
            ),
            paymentOnAccepted = apiContract.terms.payment?.onAccepted
                ?: throw NullPointerException("Contract paymentOnAccepted is null"),
            paymentOnFulfilled = apiContract.terms.payment.onFulfilled
                ?: throw NullPointerException(
                    "Contract paymentOnFulfilled is null"
                ),
            deliver = apiContract.terms.deliver?.mapNotNull { apiDeliver ->
                convertDeliver(apiDeliver ?: throw NullPointerException("Contract deliver is null"))
            } ?: throw NullPointerException("Contract deliver is null"),
            accepted = apiContract.accepted
                ?: throw NullPointerException("Contract accepted is null"),
            fulfilled = apiContract.fulfilled
                ?: throw NullPointerException("Contract fulfilled is null"),
            expiration = MDate.fromTimestamp(
                apiContract.expiration ?: throw NullPointerException("Contract expiration is null")
            ),
            deadlineToAccept = apiContract.deadlineToAccept?.let { MDate.fromTimestamp(it) }
        )
    }

    private fun convertDeliver(apiDeliver: ApiDeliver): Deliver {
        return Deliver(
            tradeSymbol = apiDeliver.tradeSymbol
                ?: throw NullPointerException("Deliver tradeSymbol is null"),
            destinationSymbol = apiDeliver.destinationSymbol
                ?: throw NullPointerException("Deliver destinationSymbol is null"),
            unitsRequired = apiDeliver.unitsRequired
                ?: throw NullPointerException("Deliver unitsRequired is null"),
            unitsFulfilled = apiDeliver.unitsFulfilled
                ?: throw NullPointerException("Deliver unitsFulfilled is null")
        )
    }
}

private interface ContractApi {

    @GET("my/contracts")
    suspend fun getMyContracts(): Response<ApiCall<List<ApiContract>>>

    @POST("my/contracts/{contractId}/accept")
    suspend fun acceptContract(@Path("contractId") contractId: String): Response<ApiCall<ApiContract>>
}