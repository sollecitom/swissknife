package sollecitom.libs.swissknife.opentelemetry.test.container.utils

import org.testcontainers.containers.Network
import org.testcontainers.grafana.LgtmStackContainer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

private const val DEFAULT_GRAFANA_STACK_DOCKER_IMAGE_NAME = "grafana/otel-lgtm"
private const val DEFAULT_GRAFANA_STACK_DOCKER_IMAGE_VERSION = "0.7.8"
private const val GRAFANA_STACK_NETWORK_ALIAS = "otel-lgtm"

private const val defaultImageName = "$DEFAULT_GRAFANA_STACK_DOCKER_IMAGE_NAME:$DEFAULT_GRAFANA_STACK_DOCKER_IMAGE_VERSION"

object GrafanaOpenTelemetryStack {

    const val OTLP_GRPC_PORT = 4317
    const val OTLP_HTTP_PORT = 4318
    const val GRAFANA_PORT = 3000
    const val PROMETHEUS_PORT = 9090

    fun newContainer(imageName: String = defaultImageName, startupAttempts: Int = 10, startupTimeout: Duration = 2.minutes): LgtmStackContainer {

        return LgtmStackContainer(imageName).withStartupAttempts(startupAttempts).withStartupTimeout(startupTimeout.toJavaDuration())
    }
}

val LgtmStackContainer.networkAlias: String get() = GRAFANA_STACK_NETWORK_ALIAS

fun LgtmStackContainer.withNetworkAndAliases(network: Network, vararg aliases: String = arrayOf(GRAFANA_STACK_NETWORK_ALIAS)): LgtmStackContainer = withNetwork(network).withNetworkAliases(*aliases)