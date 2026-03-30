package dk.mustache.spacetraders.screens.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.mustache.spacetraders.features.agent.AgentDataStore
import dk.mustache.spacetraders.common.architecture.ScreenEvent
import dk.mustache.spacetraders.features.contracts.AcceptContract
import dk.mustache.spacetraders.features.contracts.AcceptContractUseCase
import dk.mustache.spacetraders.features.contracts.ContractDataStore
import dk.mustache.spacetraders.features.trade.TradeRepository
import dk.mustache.spacetraders.features.waypoint.Waypoint
import dk.mustache.spacetraders.features.waypoint.WaypointDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    agentDataStore: AgentDataStore,
    contractDataStore: ContractDataStore,
    waypointDataStore: WaypointDataStore,
    private val acceptContractUseCase: AcceptContractUseCase,
    private val initOverviewUseCase: InitOverviewUseCase,
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

    val loadingData = initOverviewUseCase.running

    val exception = initOverviewUseCase.error.combine(acceptContractUseCase.error) { init, accept ->
        listOf(accept, init).firstOrNull { it != null }
    }

    init {
        viewModelScope.launch {
            initOverviewUseCase()
        }
    }

    fun onEvent(event: ScreenEvent) {
        when (event) {
            is ScreenEvent.ClearException -> clearExceptions()
            is AcceptContract -> acceptContract(event.contractId)
        }
    }

    private fun acceptContract(contractId: String) {
        viewModelScope.launch {
            acceptContractUseCase(contractId)
        }
    }

    private fun clearExceptions() {
        initOverviewUseCase.reset()
        acceptContractUseCase.reset()
    }
}