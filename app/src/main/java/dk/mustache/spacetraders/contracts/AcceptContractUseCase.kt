package dk.mustache.spacetraders.contracts

import dk.mustache.spacetraders.common.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AcceptContractUseCase @Inject constructor(
    private val contractRepository: ContractRepository
) : UseCase<String>() {
    suspend operator fun invoke(contractId: String) {
        logDataErrors {
            contractRepository.acceptContract(contractId)
        }
    }
}