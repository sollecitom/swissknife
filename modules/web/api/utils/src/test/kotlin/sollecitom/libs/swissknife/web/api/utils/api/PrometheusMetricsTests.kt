package sollecitom.libs.swissknife.web.api.utils.api

import assertk.assertThat
import assertk.assertions.endsWith
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
    fun `responding with the Prometheus text format when any content type is accepted`() {

        val request = Request(GET, PrometheusMetrics.PATH).header("Accept", "*/*")

        val response = metrics(request)

        assertThat(response.status).isEqualTo(Status.OK)
        assertThat(response.header("Content-Type")).isEqualTo("text/plain; version=0.0.4; charset=utf-8")
    }

    @Test
    fun `responding with the Prometheus text format when there is no accept header`() {

        val request = Request(GET, PrometheusMetrics.PATH)

        val response = metrics(request)

        assertThat(response.status).isEqualTo(Status.OK)
        assertThat(response.header("Content-Type")).isEqualTo("text/plain; version=0.0.4; charset=utf-8")
    }

    @Test
    fun `responding with the Prometheus text format when the accept header lists no content types`() {

        val request = Request(GET, PrometheusMetrics.PATH).header("Accept", "")

        val response = metrics(request)

        assertThat(response.status).isEqualTo(Status.OK)
        assertThat(response.header("Content-Type")).isEqualTo("text/plain; version=0.0.4; charset=utf-8")
    }

    @Test
    fun `responding with OpenMetrics when it is among the accepted content types`() {

        val request = Request(GET, PrometheusMetrics.PATH).header("Accept", "application/openmetrics-text;version=1.0.0;q=0.9,text/plain;version=0.0.4;q=0.5,*/*;q=0.1")

        val response = metrics(request)

        assertThat(response.status).isEqualTo(Status.OK)
        assertThat(response.header("Content-Type")).isEqualTo("application/openmetrics-text; version=1.0.0; charset=utf-8")
        assertThat(response.bodyString()).endsWith("# EOF\n")
    }
}
