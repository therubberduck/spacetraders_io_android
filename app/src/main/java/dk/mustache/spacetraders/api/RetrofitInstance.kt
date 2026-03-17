package dk.mustache.spacetraders.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthorizationInterceptor)
        .addInterceptor(SafeHttpLoggingInterceptor)
        .build()

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.spacetraders.io/v2/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}