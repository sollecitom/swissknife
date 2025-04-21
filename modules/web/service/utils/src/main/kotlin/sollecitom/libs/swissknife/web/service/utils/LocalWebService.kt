package sollecitom.libs.swissknife.web.service.utils

import org.http4k.core.HttpHandler
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.server.JettyLoom
import org.http4k.server.ServerConfig
import org.http4k.server.asServer
import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.core.domain.networking.Port
import sollecitom.libs.swissknife.core.domain.networking.RequestedPort
import sollecitom.libs.swissknife.web.service.domain.WebInterface
import sollecitom.libs.swissknife.web.service.domain.WithWebInterface


class LocalWebService(requestedPort: RequestedPort, handler: (Request) -> Response) : WithWebInterface, Startable, Stoppable {

    private val server = handler.asServer({ JettyLoom(it) }, requestedPort)
    private val port: Port get() = server.port().let(::Port)
    override val webInterface by lazy { WebInterface.local(port = port) }

    private fun HttpHandler.asServer(fn: (Int) -> ServerConfig, port: RequestedPort) = asServer(fn(port.value))

    override suspend fun start() {

        server.start()
    }

    override suspend fun stop() {

        server.stop()
    }
}