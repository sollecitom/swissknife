package sollecitom.libs.swissknife.http4k.utils

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import org.http4k.core.Method
import org.http4k.core.Request
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class RequestExtensionsTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class ContentTypeExtension {

        @Test
        fun `contentType returns null when not set`() {

            val request = Request(Method.GET, "/test")

            assertThat(request.contentType).isNull()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class HeaderExtension {

        @Test
        fun `setting a header using HttpHeader`() {

            val request = Request(Method.GET, "/test").header(HttpHeaders.Authorization, "Bearer token123")

            assertThat(request.header("authorization")).isEqualTo("Bearer token123")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class BearerTokenExtension {

        @Test
        fun `setting a bearer token`() {

            val request = Request(Method.GET, "/test").bearerToken("my-token")

            assertThat(request.header("authorization")).isEqualTo("Bearer my-token")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class ContentLengthExtension {

        @Test
        fun `setting content length with int`() {

            val request = Request(Method.GET, "/test").contentLength(42)

            assertThat(request.header("content-length")).isEqualTo("42")
        }

        @Test
        fun `setting content length with long`() {

            val request = Request(Method.GET, "/test").contentLength(12345L)

            assertThat(request.header("content-length")).isEqualTo("12345")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class BodyExtensions {

        @Test
        fun `setting JSON object body sets content type`() {

            val json = org.json.JSONObject().put("key", "value")
            val request = Request(Method.POST, "/test").body(json)

            assertThat(request.bodyString()).contains("key")
            assertThat(request.header("content-type")).isNotNull().contains("application/json")
        }

        @Test
        fun `setting JSON array body sets content type`() {

            val json = org.json.JSONArray().put("element1")
            val request = Request(Method.POST, "/test").body(json)

            assertThat(request.bodyString()).contains("element1")
            assertThat(request.header("content-type")).isNotNull().contains("application/json")
        }
    }
}
