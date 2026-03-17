package dk.mustache.spacetraders.mainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.mustache.spacetraders.agent.AgentDataStore
import dk.mustache.spacetraders.common.ScreenEvent
import dk.mustache.spacetraders.contracts.AcceptContract
import dk.mustache.spacetraders.contracts.AcceptContractUseCase
import dk.mustache.spacetraders.contracts.ContractDataStore
import dk.mustache.spacetraders.waypoint.Waypoint
import dk.mustache.spacetraders.waypoint.WaypointDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    agentDataStore: AgentDataStore,
    contractDataStore: ContractDataStore,
    waypointDataStore: WaypointDataStore,
    private val acceptContractUseCase: AcceptContractUseCase,
    private val initMyDataUseCase: InitMyDataUseCase
) : ViewModel() {
    val myAgent = agentDataStore.myAgent
    val myContracts = contractDataStore.myContracts
    val currentWayPoint = waypointDataStore.allWaypoints.map { candidates ->
        if (myAgent.value.symbol.isEmpty()) {
            Waypoint.EMPTY
        } else {
            candidates.find { candidate ->
                candidate.symbol == myAgent.value.headquarters.waypointId
            } ?: Waypoint.EMPTY
        }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Waypoint.EMPTY
    )

    val loadingData = initMyDataUseCase.running

    val exception = initMyDataUseCase.error.combine(acceptContractUseCase.error) { init, accept ->
        listOf(accept, init).firstOrNull { it != null }
    }

    init {
        viewModelScope.launch {
            initMyDataUseCase()
        }
    }

    fun onContractEvent(event: ScreenEvent) {
        when (event) {
            is ScreenEvent.ClearException -> clearExceptions()
            is AcceptContract -> acceptContract(event.contractId)
        }
    }

    private fun acceptContract(contractId: String) {
        viewModelScope.launch {
            acceptContract(contractId)
        }
    }

    private fun clearExceptions() {
        initMyDataUseCase.reset()
        acceptContractUseCase.reset()
    }
}