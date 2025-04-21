package sollecitom.libs.swissknife.web.api.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.networking.Port
import sollecitom.libs.swissknife.http4k.utils.invoke
import sollecitom.libs.swissknife.test.utils.execution.utils.test
import sollecitom.libs.swissknife.web.service.domain.WithWebInterface
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test

interface PrometheusMetricsEndpointTestSpecification : WithHttpDrivingAdapterTestSpecification {

    val metricsPath: String get() = DEFAULT_METRICS_PATH

    @Test
    fun `metrics endpoint returns response body`() = test(timeout = timeout) {

        val request = service.metricsRequest()

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.OK)
        println(response.bodyString())
    }

    @Test
    fun `metrics endpoint is exposed on the health port`() = test(timeout = timeout) {

        val request = service.metricsRequest()

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.OK)
    }

    @Test
    fun `metrics endpoint is not exposed on the main port`() = test(timeout = timeout) {

        val request = service.metricsRequest(port = service.webInterface.port)

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.NOT_FOUND)
    }

    private fun WithWebInterface.metricsRequest(port: Port = webInterface.healthPort) = Request(Method.GET, httpURLWithPath(metricsPath, port))

    companion object {
        const val DEFAULT_METRICS_PATH = "prometheus"
    }
}