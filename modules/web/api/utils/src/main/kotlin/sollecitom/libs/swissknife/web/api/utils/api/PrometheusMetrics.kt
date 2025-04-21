package sollecitom.libs.swissknife.web.api.utils.api

import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.http4k.core.*
import org.http4k.lens.Header
import org.http4k.lens.accept

internal object PrometheusMetrics {

    const val PATH = "/prometheus"

    operator fun invoke(meterRegistry: PrometheusMeterRegistry): HttpHandler = { request ->

        val requestedContentType = request.accept()?.contentTypes?.map(QualifiedContent::content)?.first() ?: ContentType.TEXT_PLAIN // TODO check what happens when the first is not supported by Prometheus but others are
        val body = meterRegistry.scrape(requestedContentType.value) // TODO use toHeaderValue() instead, as it includes the directives?
        Response(Status.OK).with(Header.CONTENT_TYPE of requestedContentType).body(body)
    }
}