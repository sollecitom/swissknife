package sollecitom.libs.swissknife.http4k.utils

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import org.http4k.core.Response
import org.http4k.core.Status
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ResponseExtensionsTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class BodyJsonObject {

        @Test
        fun `bodyJsonObject parses the response body as JSONObject`() {

            val json = JSONObject().put("name", "test")
            val response = Response(Status.OK).body(json.toString())

            val result = response.bodyJsonObject()

            assertThat(result.getString("name")).isEqualTo("test")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class BodyJsonArray {

        @Test
        fun `bodyJsonArray parses the response body as JSONArray`() {

            val json = JSONArray().put("item1").put("item2")
            val response = Response(Status.OK).body(json.toString())

            val result = response.bodyJsonArray()

            assertThat(result.length()).isEqualTo(2)
            assertThat(result.getString(0)).isEqualTo("item1")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class HeaderExtensions {

        @Test
        fun `setting a header using HttpHeader`() {

            val response = Response(Status.OK).header(HttpHeaders.ContentType, "application/json")

            assertThat(response.header("content-type")).isEqualTo("application/json")
        }

        @Test
        fun `setting content length on response`() {

            val response = Response(Status.OK).contentLength(100)

            assertThat(response.header("content-length")).isEqualTo("100")
        }

        @Test
        fun `setting content length as long on response`() {

            val response = Response(Status.OK).contentLength(999L)

            assertThat(response.header("content-length")).isEqualTo("999")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class JsonBodyExtension {

        @Test
        fun `setting JSON object body on response sets content type`() {

            val json = JSONObject().put("key", "value")
            val response = Response(Status.OK).body(json)

            assertThat(response.bodyString()).contains("key")
            assertThat(response.header("content-type")).isNotNull().contains("application/json")
        }

        @Test
        fun `setting JSON array body on response sets content type`() {

            val json = JSONArray().put("element1")
            val response = Response(Status.OK).body(json)

            assertThat(response.bodyString()).contains("element1")
            assertThat(response.header("content-type")).isNotNull().contains("application/json")
        }
    }
}
