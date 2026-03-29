package sollecitom.libs.swissknife.jwt.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.net.URI

@TestInstance(PER_CLASS)
class StringOrURITests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class CreatingFromString {

        @Test
        fun `creating from a plain string`() {

            val value = "some-issuer"

            val stringOrUri = StringOrURI(value)

            assertThat(stringOrUri.value).isEqualTo(value)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class CreatingFromURI {

        @Test
        fun `creating from a URI`() {

            val uri = URI("https://example.com/issuer")

            val stringOrUri = StringOrURI(uri)

            assertThat(stringOrUri.value).isEqualTo(uri.toString())
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Equality {

        @Test
        fun `two instances with the same value are equal`() {

            val stringOrUri1 = StringOrURI("test")
            val stringOrUri2 = StringOrURI("test")

            assertThat(stringOrUri1).isEqualTo(stringOrUri2)
        }

        @Test
        fun `URI-based and string-based instances with same value are equal`() {

            val uri = URI("https://example.com")
            val stringOrUri1 = StringOrURI(uri)
            val stringOrUri2 = StringOrURI(uri.toString())

            assertThat(stringOrUri1).isEqualTo(stringOrUri2)
        }
    }
}
