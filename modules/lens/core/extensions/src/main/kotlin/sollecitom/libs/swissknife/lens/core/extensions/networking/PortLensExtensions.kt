package sollecitom.libs.swissknife.lens.core.extensions.networking

import sollecitom.libs.swissknife.core.domain.networking.RequestedPort
import sollecitom.libs.swissknife.lens.core.extensions.requiredWithSameMetaAs
import org.http4k.config.EnvironmentKey
import org.http4k.lens.int

/** Creates a lens spec for [RequestedPort] values, backed by an integer environment variable. */
fun EnvironmentKey.requestedPort() = EnvironmentKey.int().map(::RequestedPort, RequestedPort::value)

private val servicePortKey by lazy { EnvironmentKey.requestedPort().requiredWithSameMetaAs(EnvironmentKey.k8s.SERVICE_PORT) }

/** Lens for the main service port, using the k8s SERVICE_PORT environment key. */
val EnvironmentKey.servicePort get() = servicePortKey

private val healthPortKey = EnvironmentKey.requestedPort().requiredWithSameMetaAs(EnvironmentKey.k8s.HEALTH_PORT)

/** Lens for the health check port, using the k8s HEALTH_PORT environment key. */
val EnvironmentKey.healthPort by lazy { healthPortKey }