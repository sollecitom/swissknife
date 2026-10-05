package sollecitom.libs.swissknife.openapi.validation.http4k.validator

import assertk.assertThat
import sollecitom.libs.swissknife.openapi.parser.OpenApiReader
import sollecitom.libs.swissknife.openapi.validation.http4k.validator.implementation.invoke
import sollecitom.libs.swissknife.openapi.validation.request.validator.ValidationReportError
import sollecitom.libs.swissknife.openapi.validation.request.validator.test.utils.containsOnly
import sollecitom.libs.swissknife.openapi.validation.request.validator.test.utils.hasExactlyOneErrorWithKey
import sollecitom.libs.swissknife.openapi.validation.request.validator.test.utils.hasNoErrors
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class Http4kResponseBodyOpenApiValidationTests {

    private val openApi = OpenApiReader.parseContent(
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
                "4XX":
                  description: Client error
                  content:
                    application/problem+json:
                      schema:
                        type: object
                        required: [title]
                        properties:
                          title:
                            type: string
                default:
                  description: Unexpected error
                  headers:
                    content-type:
                      schema:
                        type: string
                  content:
                    application/json:
                      schema:
                        type: object
                        required: [message]
                        properties:
                          message:
                            type: string
        """.trimIndent()
    )
    private val validator = Http4kOpenApiValidator(openApi = openApi, rejectUnknownResponseHeaders = false)

    @Test
    fun `are rejected as invalid when the body does not match the schema of the matching status range`() {

        val response = Response(Status.NOT_FOUND).header("Content-Type", "application/problem+json").body("""{"detail":"missing"}""")

        val report = validator.validate(PATH, Method.GET, ContentType.APPLICATION_JSON, response)

        assertThat(report).containsOnly(ValidationReportError.Response.Body.MissingRequiredField)
    }

    @Test
    fun `are rejected as invalid when the body does not match the schema of the default response`() {

        val response = Response(Status.INTERNAL_SERVER_ERROR).header("Content-Type", "application/json").body("""{"detail":"boom"}""")

        val report = validator.validate(PATH, Method.GET, ContentType.APPLICATION_JSON, response)

        assertThat(report).containsOnly(ValidationReportError.Response.Body.MissingRequiredField)
    }

    @Test
    fun `are validated against the media type of the response rather than the accepted one`() {

        val response = Response(Status.OK).header("Content-Type", "application/json; charset=utf-8").body("""{"name":"thing"}""")

        val report = validator.validate(PATH, Method.GET, ContentType.TEXT_PLAIN, response)

        assertThat(report).hasNoErrors()
    }

    @Test
    fun `are rejected as invalid when the body is not valid JSON`() {

        val response = Response(Status.OK).header("Content-Type", "application/json").body("""{"name":""")

        val report = validator.validate(PATH, Method.GET, ContentType.APPLICATION_JSON, response)

        assertThat(report).hasExactlyOneErrorWithKey(ValidationReportError.Response.InvalidJson.key)
    }

    @Test
    fun `are confirmed valid when only declaring the headers of the default response`() {

        val response = Response(Status.INTERNAL_SERVER_ERROR).header("Content-Type", "application/json").body("""{"message":"boom"}""")

        val report = Http4kOpenApiValidator(openApi = openApi).validate(PATH, Method.GET, ContentType.APPLICATION_JSON, response)

        assertThat(report).hasNoErrors()
    }

    private companion object {
        const val PATH = "/things"
    }
}