package dk.mustache.spacetraders.features.trade

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.updateAndGet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradeDataStore @Inject constructor() {
    private val _tradeGoods = MutableStateFlow<List<TradeGood>>(emptyList())
    val tradeGoods = _tradeGoods.asStateFlow()

    fun getTradeGood(symbol: String): Flow<TradeGood> {
        return _tradeGoods.map { list ->
            val good = list.firstOrNull { it.symbol == symbol }
            if (good == null) {
                val newGood = TradeGood(symbol, null, null)
                _tradeGoods.updateAndGet {
                    it + newGood
                }
                newGood
            } else {
                good
            }
        }
    }
}