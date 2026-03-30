package dk.mustache.spacetraders.features.contracts

import dk.mustache.spacetraders.api.helpers.DataResult
import dk.mustache.spacetraders.common.architecture.UseCase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AcceptContractUseCase @Inject constructor(
    private val contractDataStore: ContractDataStore,
    private val contractRepository: ContractRepository
) : UseCase<String>() {
    suspend operator fun invoke(contractId: String) {
        logDataErrors {
            val result = contractRepository.acceptContract(contractId)
            if (result is DataResult.Success) {
                contractDataStore.updateContract(result.data)
            }
            result
        }
    }
}