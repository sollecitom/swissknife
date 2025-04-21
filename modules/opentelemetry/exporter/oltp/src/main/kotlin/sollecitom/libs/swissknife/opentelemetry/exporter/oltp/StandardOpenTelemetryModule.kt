package sollecitom.libs.swissknife.opentelemetry.exporter.oltp

import sollecitom.libs.swissknife.opentelemetry.core.OpenTelemetryModule
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.context.propagation.TextMapPropagator
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import java.net.URI

private class StandardOpenTelemetryModule(private val endpointUrl: URI) : OpenTelemetryModule {

    override val spanExporter = spanExporter(endpointUrl)
    private val sdkTracerProvider = sdkTracerProvider(spanExporter)
    override val tracerProvider get() = sdkTracerProvider
    override val sdk = sdk(sdkTracerProvider)

    private fun spanExporter(endpointUrl: URI): OtlpGrpcSpanExporter = OtlpGrpcSpanExporter.builder().setEndpoint(endpointUrl.toString()).build()

    private fun sdkTracerProvider(spanExporter: OtlpGrpcSpanExporter): SdkTracerProvider = SdkTracerProvider.builder().addSpanProcessor(SimpleSpanProcessor.create(spanExporter)).build()

    private fun sdk(tracerProvider: SdkTracerProvider): OpenTelemetrySdk {

        val propagator: TextMapPropagator = W3CTraceContextPropagator.getInstance()
        return OpenTelemetrySdk.builder().setPropagators(ContextPropagators.create(propagator)).setTracerProvider(tracerProvider).buildAndRegisterGlobal()
    }
}

fun OpenTelemetryModule.Companion.withOpenTelemetryEndpointUrl(endpointUrl: URI): OpenTelemetryModule = StandardOpenTelemetryModule(endpointUrl)
