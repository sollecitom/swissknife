package sollecitom.libs.swissknife.opentelemetry.exporter.oltp

import sollecitom.libs.swissknife.opentelemetry.core.OpenTelemetryModule
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.context.propagation.TextMapPropagator
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

private class StandardOpenTelemetryModule(private val endpointUrl: URI) : OpenTelemetryModule {

    override val spanExporter = spanExporter(endpointUrl)
    private val sdkTracerProvider = sdkTracerProvider(spanExporter)
    override val tracerProvider get() = sdkTracerProvider
    override val sdk = sdk(sdkTracerProvider)

    override suspend fun stop() {
        withContext(Dispatchers.IO) { sdk.shutdown().join(SHUTDOWN_TIMEOUT.inWholeMilliseconds, TimeUnit.MILLISECONDS) }
    }

    private fun spanExporter(endpointUrl: URI): OtlpGrpcSpanExporter = OtlpGrpcSpanExporter.builder().setEndpoint(endpointUrl.toString()).build()

    private fun sdkTracerProvider(spanExporter: OtlpGrpcSpanExporter): SdkTracerProvider = SdkTracerProvider.builder().addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build()).build()

    private fun sdk(tracerProvider: SdkTracerProvider): OpenTelemetrySdk {

        val propagator: TextMapPropagator = W3CTraceContextPropagator.getInstance()
        return OpenTelemetrySdk.builder().setPropagators(ContextPropagators.create(propagator)).setTracerProvider(tracerProvider).buildAndRegisterGlobal()
    }

    private companion object {
        val SHUTDOWN_TIMEOUT = 10.seconds
    }
}

fun OpenTelemetryModule.Companion.withOpenTelemetryEndpointUrl(endpointUrl: URI): OpenTelemetryModule = StandardOpenTelemetryModule(endpointUrl)
