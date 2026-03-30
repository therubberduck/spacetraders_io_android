package dk.mustache.spacetraders.features.trade

import dk.mustache.spacetraders.api.RetrofitInstance
import dk.mustache.spacetraders.api.helpers.ApiCall
import dk.mustache.spacetraders.api.helpers.DataResult
import dk.mustache.spacetraders.api.helpers.richCall
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradeRepository @Inject constructor() {
    private val api = RetrofitInstance.retrofit.create(TradeApi::class.java)

    suspend fun fetchSupplyChain(): DataResult<List<SupplyChainItem>> {
        return richCall(request = {
            api.getSupplyChain()
        }, conversion = { apiSupplyChain ->
            val keys = apiSupplyChain.exportToImportMap.keys
            keys.map { key ->
                val value = apiSupplyChain.exportToImportMap[key]
                SupplyChainItem(key, value ?: emptyList())
            }
        })
    }
}

private interface TradeApi {
    @GET("market/supply-chain")
    suspend fun getSupplyChain(): Response<ApiCall<ApiSupplyChain>>
}