package dk.mustache.spacetraders.common.architecture

import dk.mustache.spacetraders.api.helpers.DataResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

abstract class UseCase<T> {
    private val _running = MutableStateFlow(false)
    val running = _running.asStateFlow()

    private val _error = MutableStateFlow<Exception?>(null)
    val error = _error.asStateFlow()

    fun reset() {
        _running.value = false
        _error.value = null
    }

    protected fun startRun() {
        _running.value = true
        _error.value = null
    }

    protected fun endRun() {
        _running.value = false
    }

    protected fun logError(e: Exception) {
        Timber.e(e)
        _error.value = e
    }

    protected suspend fun <T> logDataErrors(
        endRunOnError: Boolean = false,
        code: suspend () -> DataResult<T>
    ): Boolean {
        try {
            val result = code()
            if (result is DataResult.Success) {
                return true
            } else if (result is DataResult.Error) {
                logError(result.exception ?: Exception(result.message))
                if (endRunOnError) {
                    endRun()
                }
            }
        } catch (e: Exception) {
            logError(e)
            if (endRunOnError) {
                endRun()
            }
        }
        return false
    }
}