package dk.mustache.spacetraders.api.helpers

sealed class DataResult<T>() {
    data class Success<T>(val data: T) : DataResult<T>()
    data class Error<T>(val message: String, val exception: Exception? = null) :
        DataResult<T>()
}