package dk.mustache.spacetraders.screens.overview

import dk.mustache.spacetraders.features.agent.AgentDataStore
import dk.mustache.spacetraders.common.architecture.UseCase
import dk.mustache.spacetraders.features.contracts.ContractDataStore
import dk.mustache.spacetraders.features.fleet.FleetDataStore
import dk.mustache.spacetraders.features.waypoint.WaypointDataStore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InitOverviewUseCase @Inject constructor(
    private val agentDataStore: AgentDataStore,
    private val contractDataStore: ContractDataStore,
    private val waypointDataStore: WaypointDataStore,
) : UseCase<Unit>() {
    suspend operator fun invoke() {
        coroutineScope {
            startRun()
            val agentFetch = launch {
                logDataErrors(endRunOnError = true) {
                    agentDataStore.fetchMyAgent()
                }
            }
            val contractFetch = launch {
                logDataErrors(endRunOnError = true) {
                    contractDataStore.fetchMyContracts()
                }
            }
            val waypointFetch = launch {
                val myAgent = agentDataStore.myAgent.filter { it.isLoaded }.first()
                logDataErrors(endRunOnError = true) {
                    waypointDataStore.fetchWaypoint(myAgent.headquarters)
                }
            }
            joinAll(agentFetch, contractFetch, waypointFetch)
            endRun()
        }
    }
}