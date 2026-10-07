package sollecitom.libs.swissknife.opentelemetry.exporter.oltp

import assertk.assertThat
import assertk.assertions.isFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.opentelemetry.core.OpenTelemetryModule
import java.net.URI

@TestInstance(PER_CLASS)
class StandardOpenTelemetryModuleTests {

    @Test
    suspend fun `stopping shuts the SDK down`() {

        val module = OpenTelemetryModule.withOpenTelemetryEndpointUrl(URI("http://localhost:1"))

        module.stop()

        val span = module.tracerProvider.get("test").spanBuilder("after-stop").startSpan()
        assertThat(span.spanContext.isValid).isFalse()
    }
}
