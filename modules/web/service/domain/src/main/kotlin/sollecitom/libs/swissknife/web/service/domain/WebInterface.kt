package sollecitom.libs.swissknife.web.service.domain

import sollecitom.libs.swissknife.core.domain.networking.Port

/** Network coordinates (host, port, health port) for a web service. */
interface WebInterface {

    val host: String
    val port: Port
    val healthPort: Port

    companion object {

        private const val LOCALHOST = "localhost"

        fun create(host: String, port: Port, healthPort: Port = port): WebInterface = WebInterfaceData(host, port, healthPort)

        /** Creates a [WebInterface] bound to localhost. */
        fun local(port: Port, healthPort: Port = port): WebInterface = create(LOCALHOST, port, healthPort)
    }
}

private data class WebInterfaceData(override val host: String, override val port: Port, override val healthPort: Port) : WebInterface