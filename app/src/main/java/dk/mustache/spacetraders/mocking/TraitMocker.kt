package dk.mustache.spacetraders.mocking

import dk.mustache.spacetraders.common.dataclasses.Trait

object TraitMocker {
    fun one(
        symbol: String = "JUNGLE",
        name: String = "Jungle",
        description: String = "A lush, tropical world with dense vegetation and a thriving ecosystem, offering a wealth of resources and unique opportunities for research and exploration."
    ): Trait {
        return Trait(
            symbol = symbol,
            name = name,
            description = description
        )
    }

    fun standardSet(): List<Trait> {
        return listOf(
            one(),
            one(
                symbol = "SCATTERED_SETTLEMENTS",
                name = "Scattered Settlements",
                description = "A collection of dispersed communities, each independent yet connected through trade and communication networks."
            ),
            one(
                symbol = "FOSSILS",
                name = "Fossils",
                description = "A waypoint rich in the remains of ancient life, offering a valuable window into the past and the potential for scientific discovery."
            ),
            one(
                symbol = "MARKETPLACE",
                name = "Marketplace",
                description = "A thriving center of commerce where traders from across the galaxy gather to buy, sell, and exchange goods."
            )
        )
    }
}