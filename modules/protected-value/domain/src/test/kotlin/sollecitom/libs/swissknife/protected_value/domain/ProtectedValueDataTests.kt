package sollecitom.libs.swissknife.protected_value.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.core.domain.text.Name
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ProtectedValueDataTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class Creation {

        @Test
        fun `creating a protected value data`() {

            val bytes = "secret-data".toByteArray()
            val name = Name("api-key")
            val owner = StringId("owner-1")
            val metadata = "some-metadata"

            val protectedValue = ProtectedValueData<String, String>(bytes, name, owner, metadata)

            assertThat(protectedValue.name).isEqualTo(name)
            assertThat(protectedValue.owner).isEqualTo(owner)
            assertThat(protectedValue.metadata).isEqualTo(metadata)
            assertThat(protectedValue.value.toList()).isEqualTo(bytes.toList())
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Equality {

        @Test
        fun `two protected value data with same properties are equal`() {

            val bytes = "data".toByteArray()
            val name = Name("key")
            val owner = StringId("owner")
            val metadata = "meta"

            val pv1 = ProtectedValueData<String, String>(bytes.copyOf(), name, owner, metadata)
            val pv2 = ProtectedValueData<String, String>(bytes.copyOf(), name, owner, metadata)

            assertThat(pv1 == pv2).isTrue()
        }

        @Test
        fun `two protected value data with different values are not equal`() {

            val name = Name("key")
            val owner = StringId("owner")
            val metadata = "meta"

            val pv1 = ProtectedValueData<String, String>("data1".toByteArray(), name, owner, metadata)
            val pv2 = ProtectedValueData<String, String>("data2".toByteArray(), name, owner, metadata)

            assertThat(pv1 == pv2).isFalse()
        }

        @Test
        fun `two equal protected value data have same hash code`() {

            val bytes = "data".toByteArray()
            val name = Name("key")
            val owner = StringId("owner")
            val metadata = "meta"

            val pv1 = ProtectedValueData<String, String>(bytes.copyOf(), name, owner, metadata)
            val pv2 = ProtectedValueData<String, String>(bytes.copyOf(), name, owner, metadata)

            assertThat(pv1.hashCode()).isEqualTo(pv2.hashCode())
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class ToString {

        @Test
        fun `toString contains relevant information`() {

            val protectedValue = ProtectedValueData<String, String>("data".toByteArray(), Name("key"), StringId("owner"), "meta")

            val result = protectedValue.toString()

            assertThat(result.contains("ProtectedValueData")).isTrue()
            assertThat(result.contains("key")).isTrue()
            assertThat(result.contains("owner")).isTrue()
        }
    }
}
