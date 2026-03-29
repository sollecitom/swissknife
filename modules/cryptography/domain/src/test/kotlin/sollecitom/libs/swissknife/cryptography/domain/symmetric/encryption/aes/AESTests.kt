package sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class AESTests {

    @Test
    fun `algorithm name is AES`() {

        assertThat(AES.name).isEqualTo("AES")
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Variants {

        @Test
        fun `AES 128 has key length 128`() {

            assertThat(AES.Variant.AES_128.keyLength).isEqualTo(128)
        }

        @Test
        fun `AES 192 has key length 192`() {

            assertThat(AES.Variant.AES_192.keyLength).isEqualTo(192)
        }

        @Test
        fun `AES 256 has key length 256`() {

            assertThat(AES.Variant.AES_256.keyLength).isEqualTo(256)
        }

        @Test
        fun `AES 128 has the correct algorithm name`() {

            assertThat(AES.Variant.AES_128.algorithmName).isEqualTo("AES128")
        }

        @Test
        fun `AES 192 has the correct algorithm name`() {

            assertThat(AES.Variant.AES_192.algorithmName).isEqualTo("AES192")
        }

        @Test
        fun `AES 256 has the correct algorithm name`() {

            assertThat(AES.Variant.AES_256.algorithmName).isEqualTo("AES256")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class KeyArgumentsTests {

        @Test
        fun `KeyArguments holds the variant`() {

            val arguments = AES.KeyArguments(variant = AES.Variant.AES_256)

            assertThat(arguments.variant).isEqualTo(AES.Variant.AES_256)
        }

        @Test
        fun `KeyArguments with same variant are equal`() {

            val args1 = AES.KeyArguments(variant = AES.Variant.AES_128)
            val args2 = AES.KeyArguments(variant = AES.Variant.AES_128)

            assertThat(args1).isEqualTo(args2)
        }
    }
}
