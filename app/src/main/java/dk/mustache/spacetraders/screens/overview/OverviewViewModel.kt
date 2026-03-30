package dk.mustache.spacetraders.screens.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.mustache.spacetraders.common.architecture.ScreenEvent
import dk.mustache.spacetraders.features.agent.AgentDataStore
import dk.mustache.spacetraders.features.contracts.AcceptContract
import dk.mustache.spacetraders.features.contracts.AcceptContractUseCase
import dk.mustache.spacetraders.features.contracts.ContractDataStore
import dk.mustache.spacetraders.features.contracts.ContractModel
import dk.mustache.spacetraders.features.trade.TradeDataStore
import dk.mustache.spacetraders.features.waypoint.Waypoint
import dk.mustache.spacetraders.features.waypoint.WaypointDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverviewViewModel @Inject constructor(
    agentDataStore: AgentDataStore,
    contractDataStore: ContractDataStore,
    tradeDataStore: TradeDataStore,
    waypointDataStore: WaypointDataStore,
    private val acceptContractUseCase: AcceptContractUseCase,
    private val initOverviewUseCase: InitOverviewUseCase,
) : ViewModel() {
    val myAgent = agentDataStore.myAgent

    @OptIn(ExperimentalCoroutinesApi::class)
    val myContracts = contractDataStore.myContracts.flatMapLatest { contracts ->
        if (contracts.isEmpty()) return@flatMapLatest flowOf(emptyList())

        val contractFlows = contracts.map { contract ->
            tradeDataStore.getTradeGood(contract.deliver.firstOrNull()?.tradeSymbol ?: "")
                .map { tradeGood ->
                    ContractModel(
                        id = contract.id,
                        state = contract.state(),
                        title = contract.type + ": " + tradeGood.prettyName,
                        faction = contract.factionSymbol,
                        deadline = contract.deadlineString(),
                        expiration = contract.expirationString()
                    )
                }
        }

        combine(contractFlows) {
            it.toList()
        }
    }.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

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