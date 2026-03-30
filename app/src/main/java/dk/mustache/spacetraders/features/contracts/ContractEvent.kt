package dk.mustache.spacetraders.features.contracts

import dk.mustache.spacetraders.common.architecture.ScreenEvent

data class AcceptContract(val contractId: String) : ScreenEvent