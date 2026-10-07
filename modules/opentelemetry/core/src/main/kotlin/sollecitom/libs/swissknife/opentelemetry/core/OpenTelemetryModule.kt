package sollecitom.libs.swissknife.opentelemetry.core

import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import io.opentelemetry.api.trace.TracerProvider
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.trace.export.SpanExporter

interface OpenTelemetryModule : Stoppable {

    val sdk: OpenTelemetrySdk
    val tracerProvider: TracerProvider
    val spanExporter: SpanExporter

    /** Flushes pending spans and shuts the SDK down. */
    override suspend fun stop()

    companion object
}