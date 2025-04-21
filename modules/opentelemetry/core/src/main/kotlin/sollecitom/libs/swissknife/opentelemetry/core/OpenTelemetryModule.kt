package sollecitom.libs.swissknife.opentelemetry.core

import io.opentelemetry.api.trace.TracerProvider
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.trace.export.SpanExporter

interface OpenTelemetryModule {

    val sdk: OpenTelemetrySdk
    val tracerProvider: TracerProvider
    val spanExporter: SpanExporter

    companion object
}