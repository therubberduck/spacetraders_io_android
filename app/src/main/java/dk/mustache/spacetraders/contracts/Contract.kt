package dk.mustache.spacetraders.contracts

import dk.mustache.spacetraders.mdate.MDate
import dk.mustache.spacetraders.mdate.MDateFormat

data class Contract(
    val id: String,
    val factionSymbol: String,
    val type: String,
    val deadline: MDate,
    val paymentOnAccepted: Int,
    val paymentOnFulfilled: Int,
    val deliver: List<Deliver>,
    val accepted: Boolean,
    val fulfilled: Boolean,
    val expiration: MDate,
    val deadlineToAccept: MDate?
) {
    fun deadlineString() = deadline.show(MDateFormat.DATE_SHORT) +
            " " +
            deadline.show(MDateFormat.TIME)

    fun expirationString() = expiration.show(MDateFormat.DATE_SHORT) +
            " " +
            expiration.show(MDateFormat.TIME)

    fun state() = if (fulfilled) {
        ContractState.FULFILLED
    } else if (accepted.not()) {
        ContractState.PENDING
    } else if (deadline < MDate.now()) {
        ContractState.EXPIRED
    } else {
        ContractState.ACCEPTED
    }

    enum class ContractState {
        PENDING,
        ACCEPTED,
        EXPIRED,
        FULFILLED
    }
}
