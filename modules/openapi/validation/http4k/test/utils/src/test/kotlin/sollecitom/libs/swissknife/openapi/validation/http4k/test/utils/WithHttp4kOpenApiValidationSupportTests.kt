package sollecitom.libs.swissknife.openapi.validation.http4k.test.utils

import assertk.assertThat
import org.http4k.core.Method.GET
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.openapi.parser.OpenApiReader
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.Http4kOpenApiValidator
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.implementation.invoke

@TestInstance(PER_CLASS)
class WithHttp4kOpenApiValidationSupportTests : WithHttp4kOpenApiValidationSupport {

    override val openApiValidator = Http4kOpenApiValidator(
        openApi = OpenApiReader.parseContent(
            """
            openapi: 3.1.0
            info:
              title: Things
              version: "1.0"
            paths:
              /things:
                get:
                  responses:
                    "200":
                      description: Found
                      content:
                        application/json:
                          schema:
                            type: object
                            required: [name]
                            properties:
                              name:
                                type: string
            """.trimIndent()
        ),
        rejectUnknownResponseHeaders = false
    )

    @Test
    fun `a response to a request without a body or an accept header is checked against the JSON schema`() {

        val request = Request(GET, "/things")
        val response = Response(Status.OK).header("Content-Type", "application/json").body("""{"name":"thing"}""")

        assertThat(response).compliesWithOpenApiForRequest(request)
    }

    @Test
    fun `a response is checked against the media type the request accepts`() {

        val request = Request(GET, "/things").header("Accept", "application/json")
        val response = Response(Status.OK).header("Content-Type", "application/json").body("""{"name":"thing"}""")

        assertThat(response).compliesWithOpenApiForRequest(request)
    }
}
