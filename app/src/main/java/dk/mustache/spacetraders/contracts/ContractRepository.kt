package dk.mustache.spacetraders.contracts

import dk.mustache.spacetraders.api.ApiCall
import dk.mustache.spacetraders.api.DataResult
import dk.mustache.spacetraders.api.RetrofitInstance
import dk.mustache.spacetraders.api.richCall
import dk.mustache.spacetraders.mdate.MDate
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

    suspend fun acceptContract(contractId: String): DataResult<Unit> {
        return richCall(request = {
            api.acceptContract(contractId)
        })
    }

}

private interface ContractApi {

    @GET("my/contracts")
    suspend fun getMyContracts(): Response<ApiCall<List<ApiContract>>>

    @POST("my/contracts/{contractId}/accept")
    suspend fun acceptContract(@Path("contractId") contractId: String): Response<ApiCall<Unit>>
}