package sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class MLDSATests {

    @Test
    fun `algorithm name is ML-DSA`() {

        assertThat(MLDSA.name).isEqualTo("ML-DSA")
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Variants {

        @Test
        fun `ML-DSA 44 has the correct value`() {

            assertThat(MLDSA.Variant.ML_DSA_44.value).isEqualTo("ML-DSA-44")
        }

        @Test
        fun `ML-DSA 65 has the correct value`() {

            assertThat(MLDSA.Variant.ML_DSA_65.value).isEqualTo("ML-DSA-65")
        }

        @Test
        fun `ML-DSA 87 has the correct value`() {

            assertThat(MLDSA.Variant.ML_DSA_87.value).isEqualTo("ML-DSA-87")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class KeyPairArgumentsTests {

        @Test
        fun `KeyPairArguments holds the variant`() {

            val arguments = MLDSA.KeyPairArguments(variant = MLDSA.Variant.ML_DSA_65)

            assertThat(arguments.variant).isEqualTo(MLDSA.Variant.ML_DSA_65)
        }

        @Test
        fun `KeyPairArguments with same variant are equal`() {

            val args1 = MLDSA.KeyPairArguments(variant = MLDSA.Variant.ML_DSA_87)
            val args2 = MLDSA.KeyPairArguments(variant = MLDSA.Variant.ML_DSA_87)

            assertThat(args1).isEqualTo(args2)
        }
    }
}
