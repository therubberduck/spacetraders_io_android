package dk.mustache.spacetraders.screens.biz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dk.mustache.spacetraders.common.architecture.ScreenEvent
import dk.mustache.spacetraders.features.fleet.FleetDataStore
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BizViewModel @Inject constructor(
    fleetDataStore: FleetDataStore,
    private val initBizUseCase: InitBizUseCase
): ViewModel() {

    val myShips = fleetDataStore.myShips

    val loadingData = initBizUseCase.running

    val exception = initBizUseCase.error

    init {
        viewModelScope.launch {
            initBizUseCase()
        }
    }

    fun onEvent(event: ScreenEvent) {
        when(event) {
            is ScreenEvent.ClearException -> clearExceptions()
        }
    }

    private fun clearExceptions() {
        initBizUseCase.reset()
    }
}