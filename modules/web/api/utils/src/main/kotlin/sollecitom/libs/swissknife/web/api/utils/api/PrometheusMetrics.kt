package sollecitom.libs.swissknife.web.api.utils.api

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import io.prometheus.metrics.expositionformats.ExpositionFormats
import org.http4k.core.*

internal object PrometheusMetrics {

    const val PATH = "/prometheus"
    private val expositionFormats = ExpositionFormats.init()

    operator fun invoke(meterRegistry: PrometheusMeterRegistry): HttpHandler = { request ->

        val accept = request.header("Accept").orEmpty()
        val contentType = expositionFormats.findWriter(accept).contentType
        Response(Status.OK).header("Content-Type", contentType).body(meterRegistry.scrape(accept))
    }
}
