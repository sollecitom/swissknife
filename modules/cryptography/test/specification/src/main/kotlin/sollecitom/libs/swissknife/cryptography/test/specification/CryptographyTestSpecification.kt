package sollecitom.libs.swissknife.cryptography.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.mlkem.MLKEM
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.mlkem.MLKEM.Variant.*
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.mlkem.invoke
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa.MLDSA
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa.MLDSA.Variant.ML_DSA_87
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa.invoke
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.verify
import sollecitom.libs.swissknife.cryptography.domain.factory.CryptographicOperations
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode.GCM.Operations.Companion.DEFAULT_AUTHENTICATION_TAG_LENGTH_IN_BITS
import sollecitom.libs.swissknife.cryptography.domain.symmetric.EncryptionMode.GCM.Operations.Companion.DEFAULT_RANDOM_IV_LENGTH
import sollecitom.libs.swissknife.cryptography.domain.symmetric.decrypt
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES.Variant.AES_256
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES.Variant.AES_256_XTS
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.invoke
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


@Suppress("FunctionName")
interface CryptographyTestSpecification {

    @Test
    fun `using ML-KEM-1024 to generate and exchange an AES symmetric key securely`() {

        // Bob
        // has a public key
        val bobKeyPair = mlKem.keyPair(variant = ML_KEM_1024) // sends his public key to Alice

        // Alice
        val decodedBobPublicKey = mlKem.publicKey.from(bytes = bobKeyPair.public.encoded) // receives Bob's public key
        val (aliceSymmetricKey, encapsulation) = decodedBobPublicKey.generateEncapsulatedAESKey() // generated encryption key with encapsulation
        // sends the encapsulation to Bob

        // Bob
        // receives the encapsulation from Alice
        val bobSymmetricKey = bobKeyPair.private.decryptEncapsulatedAESKey(encapsulation) // decrypts the encapsulated key

        // Alice
        // after the handshake is completed
        val aliceMessage = "a message".toByteArray() // prepares a message for Bob
        val encryptedByAlice = aliceSymmetricKey.gcm.encryptWithRandomIV(aliceMessage) // encrypts the message using the symmetric key and sends it to Bob

        // Bob
        // receives the encrypted data from Alice
        val decryptedByBobMessage = bobSymmetricKey.gcm.decrypt(encryptedByAlice) // decrypts the message
        assertThat(decryptedByBobMessage).isEqualTo(aliceMessage)

        assertThat(aliceSymmetricKey::encoded).isEqualTo(bobSymmetricKey.encoded)
        assertThat(aliceSymmetricKey::algorithm).isEqualTo(AES.name)
        assertThat(aliceSymmetricKey).isEqualTo(bobSymmetricKey)
    }

    @Test
    fun `sending ML-KEM keys over the wire - 1024`() {

        val keyPair = mlKem.keyPair(arguments = MLKEM.KeyPairArguments(variant = ML_KEM_1024))

        val decodedPublicKey = mlKem.publicKey.from(bytes = keyPair.public.encoded)
        val decodedPrivateKey = mlKem.privateKey.from(bytes = keyPair.private.encoded)

        assertThat(keyPair.private::algorithm).isEqualTo(ML_KEM_1024.algorithmName)
        assertThat(keyPair.public::algorithm).isEqualTo(ML_KEM_1024.algorithmName)
        assertThat(decodedPrivateKey).isEqualTo(keyPair.private)
        assertThat(decodedPublicKey).isEqualTo(keyPair.public)
    }

    @Test
    fun `sending ML-KEM keys over the wire - 768`() {

        val keyPair = mlKem.keyPair(arguments = MLKEM.KeyPairArguments(variant = ML_KEM_768))

        val decodedPublicKey = mlKem.publicKey.from(bytes = keyPair.public.encoded)
        val decodedPrivateKey = mlKem.privateKey.from(bytes = keyPair.private.encoded)

        assertThat(keyPair.private::algorithm).isEqualTo(ML_KEM_768.algorithmName)
        assertThat(keyPair.public::algorithm).isEqualTo(ML_KEM_768.algorithmName)
        assertThat(decodedPrivateKey).isEqualTo(keyPair.private)
        assertThat(decodedPublicKey).isEqualTo(keyPair.public)
    }

    @Test
    fun `sending ML-KEM keys over the wire - 512`() {

        val keyPair = mlKem.keyPair(arguments = MLKEM.KeyPairArguments(variant = ML_KEM_512))

        val decodedPublicKey = mlKem.publicKey.from(bytes = keyPair.public.encoded)
        val decodedPrivateKey = mlKem.privateKey.from(bytes = keyPair.private.encoded)

        assertThat(keyPair.private::algorithm).isEqualTo(ML_KEM_512.algorithmName)
        assertThat(keyPair.public::algorithm).isEqualTo(ML_KEM_512.algorithmName)
        assertThat(decodedPrivateKey).isEqualTo(keyPair.private)
        assertThat(decodedPublicKey).isEqualTo(keyPair.public)
    }

    @Test
    fun `using ML-DSA-87 to sign and verify`() {

        val keyPair = mlDsa.keyPair(variant = ML_DSA_87)
        val message = "something to attest".toByteArray()

        val signature = keyPair.private.sign(message)
        val verifies = keyPair.public.verify(message, signature)

        assertThat(verifies).isTrue()
        assertThat(signature.metadata.keyHash.bytes).isEqualTo(keyPair.private.hash.bytes)
        assertThat(signature.metadata::algorithmName).isEqualTo(keyPair.private.algorithm)

        val notTheOriginalSigner = mlDsa.keyPair(variant = ML_DSA_87).public

        assertThat(notTheOriginalSigner.verify(message, signature)).isFalse()
    }

    @Test
    fun `sending ML-DSA keys over the wire`() {

        val keyPair = mlDsa.keyPair(arguments = MLDSA.KeyPairArguments(variant = ML_DSA_87))

        val decodedPublicKey = mlDsa.publicKey.from(bytes = keyPair.public.encoded)
        val decodedPrivateKey = mlDsa.privateKey.from(bytes = keyPair.private.encoded)

        assertThat(keyPair.private::algorithm).isEqualTo(ML_DSA_87.value)
        assertThat(keyPair.public::algorithm).isEqualTo(ML_DSA_87.value)
        assertThat(decodedPrivateKey).isEqualTo(keyPair.private)
        assertThat(decodedPublicKey).isEqualTo(keyPair.public)
    }

    @Test
    fun `encrypting and decrypting with AES-256 in GCM mode`() {

        val message = "something secret".toByteArray()
        val secretKey = aes.key(variant = AES_256)
        val decodedKey = aes.key.from(bytes = secretKey.encoded)

        val encrypted = secretKey.gcm.encryptWithRandomIV(message)
        val decrypted = decodedKey.gcm.decrypt(encrypted)

        assertThat(decrypted).isEqualTo(message)
        assertThat(encrypted.metadata::authenticationTagLengthInBits).isEqualTo(DEFAULT_AUTHENTICATION_TAG_LENGTH_IN_BITS)
        assertThat(encrypted.metadata.iv.size).isEqualTo(DEFAULT_RANDOM_IV_LENGTH)
        // GCM appends the authentication tag, so the ciphertext is longer than the plaintext.
        assertThat(encrypted.content.size).isEqualTo(message.size + DEFAULT_AUTHENTICATION_TAG_LENGTH_IN_BITS / 8)
    }

    @Test
    fun `GCM authenticates associated data without encrypting it`() {

        val message = "something secret".toByteArray()
        val associatedData = "a public header".toByteArray()
        val secretKey = aes.key(variant = AES_256)

        val encrypted = secretKey.gcm.encryptWithRandomIV(message, associatedData = associatedData)

        assertThat(secretKey.gcm.decrypt(encrypted)).isEqualTo(message)
        assertThat(encrypted.metadata.associatedData).isEqualTo(associatedData)
        assertThrows<EncryptionMode.GCM.AuthenticationTagMismatch> { secretKey.gcm.decrypt(encrypted.content, encrypted.metadata.iv, associatedData = "a different header".toByteArray()) }
    }

    @Test
    fun `GCM rejects tampered ciphertext`() {

        val message = "something secret".toByteArray()
        val secretKey = aes.key(variant = AES_256)
        val encrypted = secretKey.gcm.encryptWithRandomIV(message)

        val tampered = encrypted.content.copyOf().also { it[0] = (it[0] + 1).toByte() }

        assertThrows<EncryptionMode.GCM.AuthenticationTagMismatch> { secretKey.gcm.decrypt(tampered, encrypted.metadata.iv) }
    }

    @Test
    fun `GCM rejects the wrong key`() {

        val message = "something secret".toByteArray()
        val encrypted = aes.key(variant = AES_256).gcm.encryptWithRandomIV(message)
        val anotherKey = aes.key(variant = AES_256)

        assertThrows<EncryptionMode.GCM.AuthenticationTagMismatch> { anotherKey.gcm.decrypt(encrypted) }
    }

    @Test
    fun `encrypting and decrypting with XTS-AES-256`() {

        val message = "something secret that spans more than one block".toByteArray()
        val secretKey = aes.key(variant = AES_256_XTS)
        val decodedKey = aes.key.from(bytes = secretKey.encoded)

        val encrypted = secretKey.xts.encrypt(message, dataUnitNumber = 42)
        val decrypted = decodedKey.xts.decrypt(encrypted)

        assertThat(decrypted).isEqualTo(message)
        // XTS is length-preserving, which is what makes it suitable for sector encryption.
        assertThat(encrypted.content.size).isEqualTo(message.size)
    }

    @Test
    fun `XTS is deterministic for the same key and tweak`() {

        val message = "something secret that spans more than one block".toByteArray()
        val secretKey = aes.key(variant = AES_256_XTS)

        val first = secretKey.xts.encrypt(message, dataUnitNumber = 7)
        val second = secretKey.xts.encrypt(message, dataUnitNumber = 7)
        val underAnotherTweak = secretKey.xts.encrypt(message, dataUnitNumber = 8)

        assertThat(first.content).isEqualTo(second.content)
        assertThat(first.content).isNotEqualTo(underAnotherTweak.content)
    }

    @Test
    fun `an XTS key holds twice the material of the matching AES key`() {

        val xtsKey = aes.key(variant = AES_256_XTS)
        val plainKey = aes.key(variant = AES_256)

        assertThat(xtsKey.encoded.size).isEqualTo(plainKey.encoded.size * 2)
    }

    @Test
    fun `the single-key modes reject an XTS key`() {

        val xtsKey = aes.key(variant = AES_256_XTS)

        assertThrows<IllegalArgumentException> { xtsKey.gcm }
    }

    @Test
    fun `XTS rejects a key that does not hold two AES keys`() {

        // AES-192 is 24 bytes, which is not two AES keys, so it cannot drive XTS.
        val key = aes.key(variant = AES.Variant.AES_192)

        assertThrows<IllegalArgumentException> { key.xts.encrypt("something secret that spans a block".toByteArray(), dataUnitNumber = 1) }
    }

    @Test
    fun `an AES-256 key is also valid XTS-AES-128 material`() {

        // 32 bytes is both a single AES-256 key and two AES-128 keys: raw key bytes cannot distinguish the two,
        // so this is accepted. Generate keys with an explicit XTS variant to keep the purposes separate.
        val message = "something secret that spans more than one block".toByteArray()
        val key = aes.key(variant = AES_256)

        val encrypted = key.xts.encrypt(message, dataUnitNumber = 1)

        assertThat(key.xts.decrypt(encrypted)).isEqualTo(message)
    }

    val cryptography: CryptographicOperations
    val mlKem get() = cryptography.asymmetric.nist.mlKem
    val mlDsa get() = cryptography.asymmetric.nist.mlDsa
    val aes get() = cryptography.symmetric.aes
}
