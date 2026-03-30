package dk.mustache.spacetraders.features.trade

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale

data class TradeGood(val symbol: String, val name: String?, val description: String?) {
    val prettyName = name ?: symbol.replace("_", " ").lowercase().capitalize(Locale.current)
}
