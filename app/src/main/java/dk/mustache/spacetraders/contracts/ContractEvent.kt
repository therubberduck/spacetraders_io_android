package dk.mustache.spacetraders.contracts

interface ContractEvent {
    data class AcceptContract(val contractId: String): ContractEvent
}