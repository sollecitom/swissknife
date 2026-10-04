package sollecitom.libs.swissknife.web.api.utils.api

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class PrometheusMetricsTests {

    private val metrics = PrometheusMetrics(meterRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT))

    @Test
    fun `responding with plain text when any content type is accepted`() {

        val request = Request(GET, PrometheusMetrics.PATH).header("Accept", "*/*")

        val response = metrics(request)

        assertThat(response.status).isEqualTo(Status.OK)
        assertThat(response.header("Content-Type")).isEqualTo("text/plain; charset=utf-8")
    }

    @Test
    fun `responding with plain text when the accept header lists no content types`() {

        val request = Request(GET, PrometheusMetrics.PATH).header("Accept", "")

        val response = metrics(request)

        assertThat(response.status).isEqualTo(Status.OK)
        assertThat(response.header("Content-Type")).isEqualTo("text/plain; charset=utf-8")
    }
}