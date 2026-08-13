package sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.utils

import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.BC_PROVIDER
import sollecitom.libs.swissknife.cryptography.implementation.bouncycastle.ensureBouncyCastleProviderIsRegistered
import org.bouncycastle.jcajce.SecretKeyWithEncapsulation
import org.bouncycastle.jcajce.spec.KEMExtractSpec
import org.bouncycastle.jcajce.spec.KEMGenerateSpec
import java.security.*
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

object BouncyCastleUtils {

    init {
        // Every JCE call in this module resolves the BC provider by name, so it must be installed before any of them.
        ensureBouncyCastleProviderIsRegistered()
    }

    fun sign(privateKey: PrivateKey, message: ByteArray, signatureAlgorithm: String, provider: String): ByteArray {

        val sig: Signature = Signature.getInstance(signatureAlgorithm, provider)
        sig.initSign(privateKey, SecureRandom())
        sig.update(message, 0, message.size)
        return sig.sign()
    }

    fun verifySignature(publicKey: PublicKey, message: ByteArray, signature: ByteArray, signatureAlgorithm: String, provider: String): Boolean {

        val sig = Signature.getInstance(signatureAlgorithm, provider)

        sig.initVerify(publicKey)

        sig.update(message, 0, message.size)

        return sig.verify(signature)
    }

    fun generateSecretKey(algorithm: String, length: Int, provider: String): SecretKey {

        val keyGenerator = KeyGenerator.getInstance(algorithm, provider)
        keyGenerator.init(length)
        return keyGenerator.generateKey()
    }

    fun generateAESEncryptionKey(publicKey: PublicKey, algorithm: String, random: SecureRandom): SecretKeyWithEncapsulation {

        val keyGen = KeyGenerator.getInstance(algorithm, BC_PROVIDER)
        keyGen.init(KEMGenerateSpec(publicKey, "AES"), random)
        return keyGen.generateKey() as SecretKeyWithEncapsulation
    }

    fun decryptEncapsulatedAESKey(privateKey: PrivateKey, encapsulatedKey: ByteArray, algorithm: String, random: SecureRandom): ByteArray {
        val keyGen = KeyGenerator.getInstance(algorithm, BC_PROVIDER)
        keyGen.init(KEMExtractSpec(privateKey, encapsulatedKey, "AES"), random)
        val secEnc = keyGen.generateKey() as SecretKeyWithEncapsulation
        return secEnc.encoded
    }

    fun generateKeyPair(algorithm: String, spec: AlgorithmParameterSpec, random: SecureRandom): KeyPair {

        val kpg = KeyPairGenerator.getInstance(algorithm, BC_PROVIDER)
        kpg.initialize(spec, random)
        return kpg.generateKeyPair()
    }

    fun getPrivateKeyFromEncoded(encodedKey: ByteArray, algorithm: String): PrivateKey {

        val pkcs8EncodedKeySpec = PKCS8EncodedKeySpec(encodedKey)
        val keyFactory = KeyFactory.getInstance(algorithm, BC_PROVIDER)
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec)
    }

    fun getPublicKeyFromEncoded(encodedKey: ByteArray, algorithm: String): PublicKey {

        val x509EncodedKeySpec = X509EncodedKeySpec(encodedKey)
        val keyFactory: KeyFactory = KeyFactory.getInstance(algorithm, BC_PROVIDER)
        return keyFactory.generatePublic(x509EncodedKeySpec)
    }

    fun ctrEncrypt(key: SecretKey, iv: ByteArray, data: ByteArray): Pair<ByteArray, ByteArray> {
        val cipher = Cipher.getInstance("AES/CTR/NoPadding", BC_PROVIDER)
        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
        return cipher.iv to cipher.doFinal(data)
    }

    fun ctrDecrypt(key: SecretKey, iv: ByteArray, cipherText: ByteArray): ByteArray {
        val cipher = Cipher.getInstance("AES/CTR/NoPadding", BC_PROVIDER)
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
        return cipher.doFinal(cipherText)
    }

    fun gcmEncrypt(key: SecretKey, iv: ByteArray, data: ByteArray, associatedData: ByteArray?, tagLengthInBits: Int): ByteArray {
        val cipher = gcmCipher(Cipher.ENCRYPT_MODE, key, iv, associatedData, tagLengthInBits)
        return cipher.doFinal(data)
    }

    /** @throws AEADBadTagException when the ciphertext, the tag, or [associatedData] fail authentication. */
    fun gcmDecrypt(key: SecretKey, iv: ByteArray, cipherText: ByteArray, associatedData: ByteArray?, tagLengthInBits: Int): ByteArray {
        val cipher = gcmCipher(Cipher.DECRYPT_MODE, key, iv, associatedData, tagLengthInBits)
        return cipher.doFinal(cipherText)
    }

    private fun gcmCipher(mode: Int, key: SecretKey, iv: ByteArray, associatedData: ByteArray?, tagLengthInBits: Int): Cipher {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding", BC_PROVIDER)
        cipher.init(mode, key, GCMParameterSpec(tagLengthInBits, iv))
        associatedData?.let(cipher::updateAAD)
        return cipher
    }

    /** Encrypts a single AES block in ECB mode: the raw codebook call the XTS mode is built from. */
    fun aesEncryptBlock(key: SecretKey, block: ByteArray): ByteArray = ecbCipher(Cipher.ENCRYPT_MODE, key).doFinal(block)

    fun aesDecryptBlock(key: SecretKey, block: ByteArray): ByteArray = ecbCipher(Cipher.DECRYPT_MODE, key).doFinal(block)

    private fun ecbCipher(mode: Int, key: SecretKey): Cipher {
        val cipher = Cipher.getInstance("AES/ECB/NoPadding", BC_PROVIDER)
        cipher.init(mode, key)
        return cipher
    }
}