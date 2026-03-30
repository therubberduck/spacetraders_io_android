package dk.mustache.spacetraders.features.contracts

import dk.mustache.spacetraders.api.helpers.DataResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContractDataStore @Inject constructor(
    private val contractRepository: ContractRepository
) {
    private val _myContracts = MutableStateFlow<List<Contract>>(emptyList())
    val myContracts = _myContracts.asStateFlow()

    suspend fun fetchMyContracts(): DataResult<List<Contract>> {
        val result = contractRepository.getMyContracts()
        if (result is DataResult.Success) {
            _myContracts.value = result.data
        }
        return result
    }

    fun updateContract(contract: Contract) {
        _myContracts.update { list ->
            list.map {
                if (it.id == contract.id) {
                    contract
                } else {
                    it
                }
            }
        }
    }
}