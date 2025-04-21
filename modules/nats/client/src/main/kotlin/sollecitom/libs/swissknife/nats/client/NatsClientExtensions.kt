package sollecitom.libs.swissknife.nats.client

import sollecitom.libs.swissknife.core.domain.networking.Port
import io.nats.client.Options
import io.nats.client.impl.Headers
import java.net.URI

fun Options.Builder.server(host: String, port: Int) = server("nats://$host:$port")

fun Options.Builder.server(host: String, port: Port) = server("nats://$host:${port.value}")

fun Options.Builder.server(uri: URI) = server(uri.toString())

fun Headers.toMultiMap(): Map<String, List<String>> = entrySet().associate { it.key to it.value }