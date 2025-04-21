package sollecitom.libs.swissknife.web.api.utils.api

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.http4k.core.Filter
import org.http4k.core.HttpHandler
import org.http4k.core.Method.GET
import org.http4k.k8s.health.DefaultReadinessCheckResultRenderer
import org.http4k.k8s.health.Health
import org.http4k.k8s.health.ReadinessCheck
import org.http4k.k8s.health.ReadinessCheckResultRenderer
import org.http4k.routing.bind
import sollecitom.libs.swissknife.core.domain.networking.RequestedPort
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.web.api.domain.endpoint.Endpoint

context(_: HttpApiDefinition, _: TimeGenerator)
fun mainHttpApi(endpoints: Set<Endpoint>, requestedPort: RequestedPort, requestFilter: Filter, responseFilter: Filter) = HttpApi(endpoints, requestedPort, requestFilter, responseFilter)

fun healthHttpApi(requestedPort: RequestedPort, meterRegistry: PrometheusMeterRegistry, checks: Set<ReadinessCheck> = emptySet(), renderer: ReadinessCheckResultRenderer = DefaultReadinessCheckResultRenderer) = HttpApi(standardHealthApp(meterRegistry, checks, renderer), requestedPort)

fun standardHealthApp(meterRegistry: PrometheusMeterRegistry, checks: Set<ReadinessCheck> = emptySet(), renderer: ReadinessCheckResultRenderer = DefaultReadinessCheckResultRenderer): HttpHandler = Health(checks = checks.toList(), renderer = renderer, extraRoutes = arrayOf(PrometheusMetrics.PATH bind GET to PrometheusMetrics(meterRegistry = meterRegistry)))