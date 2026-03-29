package sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.dilithium

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class DilithiumTests {

    @Test
    fun `algorithm name is Dilithium`() {

        assertThat(Dilithium.name).isEqualTo("Dilithium")
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Variants {

        @Test
        fun `DILITHIUM 2 has the correct value`() {

            assertThat(Dilithium.Variant.DILITHIUM_2.value).isEqualTo("DILITHIUM2")
        }

        @Test
        fun `DILITHIUM 3 has the correct value`() {

            assertThat(Dilithium.Variant.DILITHIUM_3.value).isEqualTo("DILITHIUM3")
        }

        @Test
        fun `DILITHIUM 5 has the correct value`() {

            assertThat(Dilithium.Variant.DILITHIUM_5.value).isEqualTo("DILITHIUM5")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class KeyPairArgumentsTests {

        @Test
        fun `KeyPairArguments holds the variant`() {

            val arguments = Dilithium.KeyPairArguments(variant = Dilithium.Variant.DILITHIUM_3)

            assertThat(arguments.variant).isEqualTo(Dilithium.Variant.DILITHIUM_3)
        }

        @Test
        fun `KeyPairArguments with same variant are equal`() {

            val args1 = Dilithium.KeyPairArguments(variant = Dilithium.Variant.DILITHIUM_5)
            val args2 = Dilithium.KeyPairArguments(variant = Dilithium.Variant.DILITHIUM_5)

            assertThat(args1).isEqualTo(args2)
        }
    }
}
