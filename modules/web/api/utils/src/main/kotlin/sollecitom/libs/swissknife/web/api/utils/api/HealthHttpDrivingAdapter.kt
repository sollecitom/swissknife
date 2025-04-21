package sollecitom.libs.swissknife.web.api.utils.api

import sollecitom.libs.swissknife.core.domain.networking.Port
import sollecitom.libs.swissknife.core.domain.networking.RequestedPort
import sollecitom.libs.swissknife.ddd.domain.hexagonal.DrivingAdapter
import sollecitom.libs.swissknife.lens.core.extensions.networking.healthPort
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.http4k.k8s.health.DefaultReadinessCheckResultRenderer
import org.http4k.k8s.health.ReadinessCheck
import org.http4k.k8s.health.ReadinessCheckResultRenderer
import org.http4k.config.Environment
import org.http4k.config.EnvironmentKey
import org.http4k.lens.BiDiLens

class HealthHttpDrivingAdapter(configuration: Configuration, meterRegistry: PrometheusMeterRegistry, checks: Set<ReadinessCheck> = emptySet(), renderer: ReadinessCheckResultRenderer) : DrivingAdapter.WithPortBinding {

    constructor(environment: Environment, meterRegistry: PrometheusMeterRegistry, checks: Set<ReadinessCheck> = emptySet(), renderer: ReadinessCheckResultRenderer = DefaultReadinessCheckResultRenderer) : this(Configuration.from(environment), meterRegistry, checks, renderer)

    private val api = healthHttpApi(requestedPort = configuration.requestedPort, meterRegistry, checks = checks, renderer = renderer)
    override val port: Port get() = api.port

    override suspend fun start() {

        api.start()
        logger.info { "Started on port ${api.port.value}" }
    }

    override suspend fun stop() {

        api.stop()
        logger.info { "Stopped" }
    }

    data class Configuration(val requestedPort: RequestedPort) {

        companion object {
            val requestedPortKey = EnvironmentKey.healthPort

            fun from(environment: Environment, key: BiDiLens<Environment, RequestedPort> = requestedPortKey) = Configuration(requestedPort = key(environment))
        }
    }

    companion object : Loggable()
}