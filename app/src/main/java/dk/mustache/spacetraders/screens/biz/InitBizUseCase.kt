package dk.mustache.spacetraders.screens.biz

import dk.mustache.spacetraders.common.architecture.UseCase
import dk.mustache.spacetraders.features.fleet.FleetDataStore
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InitBizUseCase @Inject constructor(
    private val fleetDataStore: FleetDataStore,
) : UseCase<Unit>() {

    suspend operator fun invoke() {
        coroutineScope {
            startRun()
            val fleetFetch = launch {
                logDataErrors(endRunOnError = true) {
                    fleetDataStore.fetchMyShips()
                }
            }
            joinAll(fleetFetch)
            endRun()
        }
    }
}