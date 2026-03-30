package dk.mustache.spacetraders.features.contracts

data class ApiContract(
    val id: String?,
    val factionSymbol: String?,
    val type: String?,
    val terms: ApiTerms?,
    val accepted: Boolean?,
    val fulfilled: Boolean?,
    val expiration: String?,
    val deadlineToAccept: String?
)

data class ApiTerms(
    val deadline: String?,
    val payment: ApiPayment?,
    val deliver: List<Deliver?>?
)

data class ApiPayment(
    val onAccepted: Int?,
    val onFulfilled: Int?
)

data class Deliver(
    val tradeSymbol: String?,
    val destinationSymbol: String?,
    val unitsRequired: Int?,
    val unitsFulfilled: Int?
)
