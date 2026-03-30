package dk.mustache.spacetraders.features.fleet

import dk.mustache.spacetraders.api.helpers.DataResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FleetDataStore @Inject constructor(
    private val fleetRepository: FleetRepository
) {
    private val _myShips = MutableStateFlow<List<Ship>>(emptyList())
    val myShips = _myShips.asStateFlow()

    suspend fun fetchMyShips(): DataResult<List<Ship>> {
        val result = fleetRepository.fetchMyShips()
        if (result is DataResult.Success) {
            _myShips.value = result.data
        }
        return result
    }
}