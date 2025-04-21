package sollecitom.libs.swissknife.web.api.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.networking.Port
import sollecitom.libs.swissknife.http4k.utils.invoke
import sollecitom.libs.swissknife.test.utils.execution.utils.test
import sollecitom.libs.swissknife.web.service.domain.WithWebInterface
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test

interface MonitoringEndpointsTestSpecification : WithHttpDrivingAdapterTestSpecification {

    val livenessPath: String get() = DEFAULT_LIVENESS_PATH
    val readinessPath: String get() = DEFAULT_READINESS_PATH

    @Test
    fun `liveness endpoint is exposed on the health port`() = test(timeout = timeout) {

        val request = service.livenessRequest()

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.OK)
    }

    @Test
    fun `liveness endpoint is not exposed on the main port`() = test(timeout = timeout) {

        val request = service.livenessRequest(port = service.webInterface.port)

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.NOT_FOUND)
    }

    @Test
    fun `readiness endpoint is exposed on the health port`() = test(timeout = timeout) {

        val request = service.readinessRequest()

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.OK)
    }

    @Test
    fun `readiness endpoint is not exposed on the main port`() = test(timeout = timeout) {

        val request = service.readinessRequest(port = service.webInterface.port)

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.NOT_FOUND)
    }

    private fun WithWebInterface.livenessRequest(port: Port = webInterface.healthPort) = Request(GET, httpURLWithPath(livenessPath, port))

    private fun WithWebInterface.readinessRequest(port: Port = webInterface.healthPort) = Request(GET, httpURLWithPath(readinessPath, port))

    companion object {
        const val DEFAULT_LIVENESS_PATH = "liveness"
        const val DEFAULT_READINESS_PATH = "readiness"
    }
}