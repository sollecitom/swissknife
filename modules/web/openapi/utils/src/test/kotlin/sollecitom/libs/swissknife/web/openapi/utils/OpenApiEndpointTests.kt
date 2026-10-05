package sollecitom.libs.swissknife.web.openapi.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class OpenApiEndpointTests {

    private val endpoint = OpenApiEndpoint(openApiLocation = "api/api.yml")

    @Test
    fun `responding with the supported content type rather than the requested directives`() {

        val request = Request(GET, endpoint.path).header("Accept", "application/json; charset=utf-16")

        val response = endpoint.route(request)

        assertThat(response.status).isEqualTo(Status.OK)
        assertThat(response.header("Content-Type")).isEqualTo("application/json; charset=utf-8")
    }

    @Test
    fun `responding with YAML when no supported content type is accepted`() {

        val request = Request(GET, endpoint.path).header("Accept", "text/html")

        val response = endpoint.route(request)

        assertThat(response.status).isEqualTo(Status.OK)
        assertThat(response.header("Content-Type")).isEqualTo("application/yaml; charset=utf-8")
    }
}
