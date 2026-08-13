package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.symmetric.encryption.xts

import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

/**
 * XTS-AES as specified by IEEE Std 1619-2007, built on the raw AES codebook.
 *
 * Bouncy Castle exposes no IEEE 1619 XTS: its only XTS type, `KXTSBlockCipher`, implements the DSTU 7624
 * (Kalyna) variant, which uses a single key and a different tweak derivation, so it is not interoperable.
 * The mode is therefore composed here from single-block AES calls, which is all XTS is defined to be.
 *
 * Validated against the IEEE 1619 test vectors (see `XtsAesTests`).
 */
internal object XtsAes {

    private const val BLOCK_LENGTH = 16

    /** The reduction polynomial for GF(2^128), x^128 + x^7 + x^2 + x + 1, in its low-byte form. */
    private const val GF_128_REDUCTION_BYTE = 0x87

    fun encrypt(key: ByteArray, tweak: ByteArray, data: ByteArray): ByteArray = transform(key, tweak, data, encrypting = true)

    fun decrypt(key: ByteArray, tweak: ByteArray, data: ByteArray): ByteArray = transform(key, tweak, data, encrypting = false)

    /** Encodes a data unit number as a tweak: 16 bytes, little-endian, as IEEE 1619 prescribes. */
    fun tweakOf(dataUnitNumber: Long): ByteArray {
        require(dataUnitNumber >= 0) { "the data unit number must not be negative but was $dataUnitNumber" }
        val tweak = ByteArray(BLOCK_LENGTH)
        for (index in 0 until Long.SIZE_BYTES) {
            tweak[index] = (dataUnitNumber ushr (8 * index)).toByte()
        }
        return tweak
    }

    private fun transform(key: ByteArray, tweak: ByteArray, data: ByteArray, encrypting: Boolean): ByteArray {

        val (dataKey, tweakKey) = key.splitIntoAesKeyPair()
        require(tweak.size == EncryptionMode.XTS.Operations.TWEAK_LENGTH) { "an XTS tweak must be exactly ${EncryptionMode.XTS.Operations.TWEAK_LENGTH} bytes but was ${tweak.size}" }
        require(data.size >= EncryptionMode.XTS.Operations.MINIMUM_DATA_LENGTH) { "XTS needs at least ${EncryptionMode.XTS.Operations.MINIMUM_DATA_LENGTH} bytes because ciphertext stealing requires a full block, but got ${data.size}" }

        val fullBlocks = data.size / BLOCK_LENGTH
        val remainder = data.size % BLOCK_LENGTH
        // The last full block is held back when stealing, because it must be encrypted under the following tweak.
        val plainBlocks = if (remainder == 0) fullBlocks else fullBlocks - 1

        val result = ByteArray(data.size)
        var currentTweak = BouncyCastleUtils.aesEncryptBlock(tweakKey, tweak)

        repeat(plainBlocks) { index ->
            val offset = index * BLOCK_LENGTH
            val block = data.copyOfRange(offset, offset + BLOCK_LENGTH)
            applyCodebook(dataKey, block, currentTweak, encrypting).copyInto(result, offset)
            currentTweak = currentTweak.multipliedByAlpha()
        }
        if (remainder == 0) return result

        // Ciphertext stealing. Decryption consumes the two final tweaks in the opposite order to encryption.
        val nextTweak = currentTweak.multipliedByAlpha()
        val penultimateTweak = if (encrypting) currentTweak else nextTweak
        val finalTweak = if (encrypting) nextTweak else currentTweak

        val penultimateOffset = plainBlocks * BLOCK_LENGTH
        val stolenSource = applyCodebook(dataKey, data.copyOfRange(penultimateOffset, penultimateOffset + BLOCK_LENGTH), penultimateTweak, encrypting)

        val remainderOffset = fullBlocks * BLOCK_LENGTH
        stolenSource.copyInto(result, remainderOffset, 0, remainder)

        val stolenBlock = stolenSource.copyOf()
        data.copyInto(stolenBlock, 0, remainderOffset, remainderOffset + remainder)
        applyCodebook(dataKey, stolenBlock, finalTweak, encrypting).copyInto(result, penultimateOffset)

        return result
    }

    /** The XEX core of XTS: XOR with the tweak, run the codebook, XOR with the tweak again. */
    private fun applyCodebook(dataKey: SecretKey, block: ByteArray, tweak: ByteArray, encrypting: Boolean): ByteArray {
        val masked = block xor tweak
        val transformed = if (encrypting) BouncyCastleUtils.aesEncryptBlock(dataKey, masked) else BouncyCastleUtils.aesDecryptBlock(dataKey, masked)
        return transformed xor tweak
    }

    /** Multiplies by the primitive element α of GF(2^128): a little-endian shift left by one bit, then reduce. */
    private fun ByteArray.multipliedByAlpha(): ByteArray {
        val result = ByteArray(size)
        var carry = 0
        for (index in indices) {
            val current = this[index].toInt() and 0xFF
            result[index] = (((current shl 1) or carry) and 0xFF).toByte()
            carry = (current ushr 7) and 1
        }
        if (carry != 0) result[0] = (result[0].toInt() xor GF_128_REDUCTION_BYTE).toByte()
        return result
    }

    private infix fun ByteArray.xor(other: ByteArray) = ByteArray(size) { index -> (this[index].toInt() xor other[index].toInt()).toByte() }

    /** An XTS key is the data key followed by the tweak key, each a full-length AES key. */
    private fun ByteArray.splitIntoAesKeyPair(): Pair<SecretKey, SecretKey> {
        val validLengths = listOf(AES.Variant.AES_128_XTS, AES.Variant.AES_256_XTS).map { it.keyLength / 8 }
        require(size in validLengths) { "an XTS key must hold two AES keys, so it must be ${validLengths.joinToString(" or ")} bytes, but was $size: generate it with AES.Variant.AES_128_XTS or AES.Variant.AES_256_XTS" }
        val half = size / 2
        return SecretKeySpec(copyOfRange(0, half), AES.name) to SecretKeySpec(copyOfRange(half, size), AES.name)
    }
}
