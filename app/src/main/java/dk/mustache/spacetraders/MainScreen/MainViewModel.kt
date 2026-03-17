package dk.mustache.spacetraders.MainScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.mustache.spacetraders.agent.Agent
import dk.mustache.spacetraders.api.NetworkResult
import dk.mustache.spacetraders.api.Repository
import dk.mustache.spacetraders.common.WaypointId
import dk.mustache.spacetraders.contracts.Contract
import dk.mustache.spacetraders.contracts.ContractEvent
import dk.mustache.spacetraders.waypoint.Waypoint
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _agent = MutableStateFlow(Agent.EMPTY)
    val agent: StateFlow<Agent> = _agent.asStateFlow()

    private val _contracts = MutableStateFlow<List<Contract>>(emptyList())
    val contracts: StateFlow<List<Contract>> = _contracts.asStateFlow()

    private val _currentWayPoint = MutableStateFlow<Waypoint>(Waypoint.EMPTY)
    val currentWayPoint: StateFlow<Waypoint> = _currentWayPoint

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _showSuccess = MutableStateFlow(false)
    val showSuccess: StateFlow<Boolean> = _showSuccess.asStateFlow()

    init {
        viewModelScope.launch {
            _loading.value = true
            val agentToken = async {
                val result = repository.getMyAgent()
                if (result is NetworkResult.Success) {
                    _agent.value = result.data
                } else if (result is NetworkResult.Error) {
                    Timber.e("Error fetching agent data: ${result.message}")
                }
            }
            val contractToken = async {
                fetchContracts()
            }
        }
        viewModelScope.launch {
            agent.collect { agent ->
                if (agent.isLoaded) {
                    fetchHomeWaypoint(agent.headquarters)
                    _loading.value = false
                }
            }
        }
    }

    private suspend fun fetchHomeWaypoint(waypointId: WaypointId) {
        val result = repository.getWaypoint(waypointId)
        if (result is NetworkResult.Success) {
            _currentWayPoint.value = result.data
        } else if (result is NetworkResult.Error) {
            Timber.e("Error fetching waypoint data: ${result.message}")
        }
    }

    private suspend fun fetchContracts() {
        val result = repository.getMyContracts()
        if (result is NetworkResult.Success) {
            _contracts.value = result.data
        } else if (result is NetworkResult.Error) {
            Timber.e("Error fetching contract data: ${result.message}")
        }
    }

    fun onContractEvent(event: ContractEvent) {
        when(event) {
            is ContractEvent.AcceptContract -> acceptContract(event.contractId)
        }
    }

    private fun acceptContract(contractId: String) {
        viewModelScope.launch {
            val result = repository.acceptContract(contractId)
            if (result is NetworkResult.Success) {
                fetchContracts()
            }
            else if (result is NetworkResult.Error) {
                Timber.e("Error accepting contract: ${result.message}")
            }
        }
    }
}