package dk.mustache.spacetraders.api.helpers

data class ApiCall<T>(val data: T, val meta: ApiMeta?)

data class ApiMeta(val total: Int, val page: Int, val limit: Int)
