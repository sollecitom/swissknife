package sollecitom.libs.swissknife.core.domain.error

class DownstreamServiceUnavailableException(cause: Throwable? = null, message: String = "A downstream service is temporarily unavailable") : IllegalStateException(message, cause) {

    companion object {

        fun forService(service: String, cause: Throwable? = null) = DownstreamServiceUnavailableException(message = "Downstream service '$service' is temporarily unavailable", cause = cause)
    }
}