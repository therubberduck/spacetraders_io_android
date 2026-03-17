package dk.mustache.spacetraders.api

import okhttp3.Interceptor
import okhttp3.Response

object AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $TOKEN")
            .build()
        return chain.proceed(request)
    }
}