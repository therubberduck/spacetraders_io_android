package dk.mustache.spacetraders.mocking

import dk.mustache.spacetraders.common.MDate
import dk.mustache.spacetraders.features.contracts.Contract
import dk.mustache.spacetraders.features.contracts.ContractModel
import dk.mustache.spacetraders.features.contracts.Deliver

object ContractMocker {
    fun one(
        id: String = "123",
        factionSymbol: String = "GALACTIC",
        type: String = "OFFER",
        deadline: MDate = MDate.Builder().now(),
        paymentOnAccepted: Int = 1471,
        paymentOnFulfilled: Int = 8374,
        deliver: List<Deliver> = listOf(
            Deliver(
                tradeSymbol = "COPPER_ORE",
                destinationSymbol = "X1-HF93-A1",
                unitsRequired = 100,
                unitsFulfilled = 0
            )
        ),
        accepted: Boolean = false,
        fulfilled: Boolean = false,
        expiration: MDate = MDate.Builder().now(),
        deadlineToAccept: MDate = MDate.Builder().now()
    ): Contract {
        return Contract(
            id = id,
            factionSymbol = factionSymbol,
            type = type,
            deadline = deadline,
            paymentOnAccepted = paymentOnAccepted,
            paymentOnFulfilled = paymentOnFulfilled,
            deliver = deliver,
            accepted = accepted,
            fulfilled = fulfilled,
            expiration = expiration,
            deadlineToAccept = deadlineToAccept
        )
    }

    fun standardSet(): List<Contract> {
        return listOf(
            one()
        )
    }

    fun oneModel(): ContractModel {
        return ContractModel(
            id = "123",
            state = Contract.ContractState.PENDING,
            title = "Offer: Copper Ore",
            faction = "Galactic Empire",
            deadline = "2023-01-01",
            expiration = "2023-01-01"
        )
    }

    fun standardSetModel(): List<ContractModel> {
        return listOf(
            oneModel()
        )
    }
}