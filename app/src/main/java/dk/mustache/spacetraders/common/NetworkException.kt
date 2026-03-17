package dk.mustache.spacetraders.common

class NetworkException(val code: Int, message: String, cause: Exception? = null) :
    Exception(message, cause)