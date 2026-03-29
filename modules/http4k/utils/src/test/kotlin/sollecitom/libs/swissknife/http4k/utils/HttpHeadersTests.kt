package sollecitom.libs.swissknife.http4k.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class HttpHeadersTests {

    @Test
    fun `ContentType header has the correct name`() {

        assertThat(HttpHeaders.ContentType.name).isEqualTo("content-type")
    }

    @Test
    fun `ContentLength header has the correct name`() {

        assertThat(HttpHeaders.ContentLength.name).isEqualTo("content-length")
    }

    @Test
    fun `Location header has the correct name`() {

        assertThat(HttpHeaders.Location.name).isEqualTo("location")
    }

    @Test
    fun `Authorization header has the correct name`() {

        assertThat(HttpHeaders.Authorization.name).isEqualTo("authorization")
    }
}
