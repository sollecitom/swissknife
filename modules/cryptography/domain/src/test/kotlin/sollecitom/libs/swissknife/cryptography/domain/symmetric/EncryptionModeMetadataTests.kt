package sollecitom.libs.swissknife.cryptography.domain.symmetric

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.isNull
import sollecitom.libs.swissknife.cryptography.domain.key.CryptographicKey
import sollecitom.libs.swissknife.hashing.utils.Hash
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class EncryptionModeMetadataTests {

    private val keyMetadata = CryptographicKey.Metadata(algorithm = "AES", format = "RAW", hash = object : Hash {
        override val bytes = byteArrayOf(10, 20)
    })

    @Nested
    @TestInstance(PER_CLASS)
    inner class Gcm {

        private fun metadata(iv: ByteArray = byteArrayOf(1, 2, 3), tagLength: Int = 128, associatedData: ByteArray? = byteArrayOf(9)) = EncryptionMode.GCM.Metadata(iv = iv, authenticationTagLengthInBits = tagLength, associatedData = associatedData, key = keyMetadata)

        @Test
        fun `metadata with the same values are equal`() {

            assertThat(metadata()).isEqualTo(metadata())
            assertThat(metadata().hashCode()).isEqualTo(metadata().hashCode())
        }

        @Test
        fun `metadata with a different IV are not equal`() {

            assertThat(metadata(iv = byteArrayOf(1))).isNotEqualTo(metadata(iv = byteArrayOf(2)))
        }

        @Test
        fun `metadata with a different tag length are not equal`() {

            assertThat(metadata(tagLength = 128)).isNotEqualTo(metadata(tagLength = 96))
        }

        @Test
        fun `metadata with different associated data are not equal`() {

            assertThat(metadata(associatedData = byteArrayOf(1))).isNotEqualTo(metadata(associatedData = byteArrayOf(2)))
            assertThat(metadata(associatedData = null)).isNotEqualTo(metadata(associatedData = byteArrayOf(1)))
        }

        @Test
        fun `associated data is optional`() {

            assertThat(metadata(associatedData = null).associatedData).isNull()
            assertThat(metadata(associatedData = null)).isEqualTo(metadata(associatedData = null))
        }

        @Test
        fun `toString includes the mode metadata`() {

            assertThat(metadata().toString()).contains("Metadata")
        }

        @Test
        fun `the defaults follow the NIST recommendation`() {

            assertThat(EncryptionMode.GCM.Operations.DEFAULT_RANDOM_IV_LENGTH).isEqualTo(12)
            assertThat(EncryptionMode.GCM.Operations.DEFAULT_AUTHENTICATION_TAG_LENGTH_IN_BITS).isEqualTo(128)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Xts {

        private fun metadata(tweak: ByteArray = byteArrayOf(1, 2, 3)) = EncryptionMode.XTS.Metadata(tweak = tweak, key = keyMetadata)

        @Test
        fun `metadata with the same values are equal`() {

            assertThat(metadata()).isEqualTo(metadata())
            assertThat(metadata().hashCode()).isEqualTo(metadata().hashCode())
        }

        @Test
        fun `metadata with a different tweak are not equal`() {

            assertThat(metadata(tweak = byteArrayOf(1))).isNotEqualTo(metadata(tweak = byteArrayOf(2)))
        }

        @Test
        fun `toString includes the mode metadata`() {

            assertThat(metadata().toString()).contains("Metadata")
        }

        @Test
        fun `a tweak is one block and stealing needs one block`() {

            assertThat(EncryptionMode.XTS.Operations.TWEAK_LENGTH).isEqualTo(16)
            assertThat(EncryptionMode.XTS.Operations.MINIMUM_DATA_LENGTH).isEqualTo(16)
        }
    }
}
