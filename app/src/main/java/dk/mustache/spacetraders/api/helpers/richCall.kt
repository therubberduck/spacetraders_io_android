package dk.mustache.spacetraders.api.helpers

import retrofit2.Response

suspend inline fun <T, reified U> richCall(
    request: suspend () -> Response<ApiCall<T>>,
    noinline conversion: ((T) -> U)? = null
): DataResult<U> {
    return try {
        val response = request()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                if (conversion != null)
                    try {
                        DataResult.Success(conversion(body.data))
                    } catch (e: Exception) {
                        DataResult.Error("Conversion failed: ${e.message}", e)
                    }
                else if (body.data is U)
                    DataResult.Success(body.data as U)
                else
                    DataResult.Error("Conversion failed")
            } else {
                DataResult.Error(
                    "Response body is null",
                    NullPointerException("Response body is null")
                )
            }
        } else {
            DataResult.Error(
                "Error: ${response.code()} ${response.message()}",
                NetworkException(response.code(), response.message())
            )
        }
    } catch (e: Exception) {
        DataResult.Error(e.message ?: "Unknown error occurred", e)
    }
}

