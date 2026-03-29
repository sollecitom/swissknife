package sollecitom.libs.swissknife.ddd.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class HappeningTypeTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class Creation {

        @Test
        fun `creating a happening type`() {

            val name = Name("some-event")
            val version = IntVersion(1)

            val type = Happening.Type(name, version)

            assertThat(type.name).isEqualTo(name)
            assertThat(type.version).isEqualTo(version)
        }

        @Test
        fun `string value follows expected format`() {

            val name = Name("some-event")
            val version = IntVersion(3)

            val type = Happening.Type(name, version)

            assertThat(type.stringValue).isEqualTo("some-event--v3")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Parsing {

        @Test
        fun `parsing a valid type string`() {

            val rawValue = "order-placed--v2"

            val type = Happening.Type.parse(rawValue)

            assertThat(type.name).isEqualTo(Name("order-placed"))
            assertThat(type.version).isEqualTo(IntVersion(2))
        }

        @Test
        fun `parsing a type string and converting back to string`() {

            val rawValue = "payment-received--v5"

            val type = Happening.Type.parse(rawValue)

            assertThat(type.stringValue).isEqualTo(rawValue)
        }

        @Test
        fun `parsing an invalid type string`() {

            val invalidValue = "no-version-here"

            val result = runCatching { Happening.Type.parse(invalidValue) }

            assertThat(result).isFailure().isInstanceOf<IllegalStateException>()
        }
    }
}
