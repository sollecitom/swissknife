package sollecitom.libs.swissknife.web.openapi.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import assertk.assertions.isNotNull
import sollecitom.libs.swissknife.http4k.utils.invoke
import sollecitom.libs.swissknife.test.utils.execution.utils.test
import sollecitom.libs.swissknife.web.api.test.utils.WithHttpDrivingAdapterTestSpecification
import sollecitom.libs.swissknife.web.api.test.utils.httpURLWithPath
import sollecitom.libs.swissknife.web.api.test.utils.isEqualToIgnoringDirectives
import org.http4k.core.*
import org.http4k.core.Method.GET
import org.http4k.lens.accept
import org.http4k.lens.contentType
import org.junit.jupiter.api.Test

interface OpenApiHttpEndpointTestSpecification : WithHttpDrivingAdapterTestSpecification {

    val openApiPath: String get() = defaultOpenApiPath

    @Test
    fun `retrieving the OpenApi definition as YAML`() = test(expectedContentType = ContentType.APPLICATION_YAML, accepted = listOf(ContentType.APPLICATION_YAML))

    @Test
    fun `retrieving the OpenApi definition as JSON`() = test(expectedContentType = ContentType.APPLICATION_JSON, accepted = listOf(ContentType.APPLICATION_JSON))

    @Test
    fun `retrieving the OpenApi definition without specifying a content type`() = test(expectedContentType = ContentType.APPLICATION_YAML, accepted = emptyList())

    private fun test(expectedContentType: ContentType, accepted: List<ContentType>) = test(timeout = timeout) {

        val request = Request(GET, service.httpURLWithPath(openApiPath)).accept(Accept(contentTypes = accepted.map(::QualifiedContent)))

        val response = httpClient(request)

        assertThat(response.status).isEqualTo(Status.OK)
        assertThat(response.contentType()).isNotNull().isEqualToIgnoringDirectives(expectedContentType)
        assertThat(response.bodyString()).isNotEmpty()
    }

    private companion object {
        const val defaultOpenApiPath = "api"
    }
}