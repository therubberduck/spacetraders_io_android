package dk.mustache.spacetraders.api

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

object ResponseBuilder {
    fun makeNewResponse(chain: Interceptor.Chain, message: String, code: Int = 6969): Response {
        return Response.Builder().apply {
            protocol(Protocol.HTTP_1_1)
            code(code)
            message(message)
            body("".toResponseBody())
            request(chain.request())
        }.build()
    }
}