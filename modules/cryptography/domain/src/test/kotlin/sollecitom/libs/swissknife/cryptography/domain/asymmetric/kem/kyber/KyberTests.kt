package sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.kyber

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class KyberTests {

    @Test
    fun `algorithm name is KYBER`() {

        assertThat(Kyber.name).isEqualTo("KYBER")
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Variants {

        @Test
        fun `KYBER 512 has key length 512`() {

            assertThat(Kyber.Variant.KYBER_512.keyLength).isEqualTo(512)
        }

        @Test
        fun `KYBER 768 has key length 768`() {

            assertThat(Kyber.Variant.KYBER_768.keyLength).isEqualTo(768)
        }

        @Test
        fun `KYBER 1024 has key length 1024`() {

            assertThat(Kyber.Variant.KYBER_1024.keyLength).isEqualTo(1024)
        }

        @Test
        fun `KYBER 512 has the correct algorithm name`() {

            assertThat(Kyber.Variant.KYBER_512.algorithmName).isEqualTo("ML-KEM-512")
        }

        @Test
        fun `KYBER 768 has the correct algorithm name`() {

            assertThat(Kyber.Variant.KYBER_768.algorithmName).isEqualTo("ML-KEM-768")
        }

        @Test
        fun `KYBER 1024 has the correct algorithm name`() {

            assertThat(Kyber.Variant.KYBER_1024.algorithmName).isEqualTo("ML-KEM-1024")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class KeyPairArgumentsTests {

        @Test
        fun `KeyPairArguments holds the variant`() {

            val arguments = Kyber.KeyPairArguments(variant = Kyber.Variant.KYBER_768)

            assertThat(arguments.variant).isEqualTo(Kyber.Variant.KYBER_768)
        }

        @Test
        fun `KeyPairArguments with same variant are equal`() {

            val args1 = Kyber.KeyPairArguments(variant = Kyber.Variant.KYBER_1024)
            val args2 = Kyber.KeyPairArguments(variant = Kyber.Variant.KYBER_1024)

            assertThat(args1).isEqualTo(args2)
        }
    }
}
