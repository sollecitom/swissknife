package sollecitom.libs.swissknife.web.api.utils.api

import sollecitom.libs.swissknife.core.domain.networking.RequestedPort
import sollecitom.libs.swissknife.ddd.domain.hexagonal.DrivingAdapter
import sollecitom.libs.swissknife.lens.core.extensions.networking.servicePort
import org.http4k.config.Environment
import org.http4k.config.EnvironmentKey
import org.http4k.core.HttpHandler
import org.http4k.lens.BiDiLens

interface HttpDrivingAdapter : DrivingAdapter.WithPortBinding, HttpHandler {

    data class Configuration(val requestedPort: RequestedPort) {

        companion object {
            val requestedPortKey = EnvironmentKey.servicePort

            fun from(environment: Environment, key: BiDiLens<Environment, RequestedPort> = requestedPortKey) = Configuration(requestedPort = key(environment))
        }
    }

    companion object
}