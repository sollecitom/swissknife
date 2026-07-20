package sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.mlkem

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class MLKEMTests {

    @Test
    fun `algorithm name is ML-KEM`() {

        assertThat(MLKEM.name).isEqualTo("ML-KEM")
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Variants {

        @Test
        fun `ML-KEM 512 has key length 512`() {

            assertThat(MLKEM.Variant.ML_KEM_512.keyLength).isEqualTo(512)
        }

        @Test
        fun `ML-KEM 768 has key length 768`() {

            assertThat(MLKEM.Variant.ML_KEM_768.keyLength).isEqualTo(768)
        }

        @Test
        fun `ML-KEM 1024 has key length 1024`() {

            assertThat(MLKEM.Variant.ML_KEM_1024.keyLength).isEqualTo(1024)
        }

        @Test
        fun `ML-KEM 512 has the correct algorithm name`() {

            assertThat(MLKEM.Variant.ML_KEM_512.algorithmName).isEqualTo("ML-KEM-512")
        }

        @Test
        fun `ML-KEM 768 has the correct algorithm name`() {

            assertThat(MLKEM.Variant.ML_KEM_768.algorithmName).isEqualTo("ML-KEM-768")
        }

        @Test
        fun `ML-KEM 1024 has the correct algorithm name`() {

            assertThat(MLKEM.Variant.ML_KEM_1024.algorithmName).isEqualTo("ML-KEM-1024")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class KeyPairArgumentsTests {

        @Test
        fun `KeyPairArguments holds the variant`() {

            val arguments = MLKEM.KeyPairArguments(variant = MLKEM.Variant.ML_KEM_768)

            assertThat(arguments.variant).isEqualTo(MLKEM.Variant.ML_KEM_768)
        }

        @Test
        fun `KeyPairArguments with same variant are equal`() {

            val args1 = MLKEM.KeyPairArguments(variant = MLKEM.Variant.ML_KEM_1024)
            val args2 = MLKEM.KeyPairArguments(variant = MLKEM.Variant.ML_KEM_1024)

            assertThat(args1).isEqualTo(args2)
        }
    }
}
