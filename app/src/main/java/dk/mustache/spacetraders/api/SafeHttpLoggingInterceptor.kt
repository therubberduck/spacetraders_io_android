package dk.mustache.spacetraders.api

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

object SafeHttpLoggingInterceptor : Interceptor {
    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            interceptor.intercept(chain)
        } catch (e: Throwable) {
            Timber.e(e)
            ResponseBuilder.makeNewResponse(
                chain,
                e.message ?: "Error Occurred in SafeHttpLoggingInterceptor"
            )
        }
    }
}