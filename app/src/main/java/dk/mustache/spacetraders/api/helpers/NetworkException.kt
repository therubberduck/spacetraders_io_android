package dk.mustache.spacetraders.api.helpers

class NetworkException(val code: Int, message: String, cause: Exception? = null) :
    Exception(message, cause)