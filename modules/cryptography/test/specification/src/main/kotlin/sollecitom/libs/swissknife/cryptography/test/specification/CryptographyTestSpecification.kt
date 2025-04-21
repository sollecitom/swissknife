package sollecitom.libs.swissknife.cryptography.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.kyber.Kyber
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.kyber.Kyber.Variant.*
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.kyber.invoke
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.dilithium.Dilithium
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.dilithium.Dilithium.Variant.DILITHIUM_5
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.dilithium.invoke
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.verify
import sollecitom.libs.swissknife.cryptography.domain.factory.CryptographicOperations
import sollecitom.libs.swissknife.cryptography.domain.symmetric.decrypt
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.AES.Variant.AES_256
import sollecitom.libs.swissknife.cryptography.domain.symmetric.encryption.aes.invoke
import org.junit.jupiter.api.Test


@Suppress("FunctionName")
interface CryptographyTestSpecification {

    @Test
    fun `using Kyber-1024-AES to generate and exchange a symmetric key securely`() {

        // Bob
        // has a public key
        val bobKeyPair = kyber.keyPair(variant = KYBER_1024) // sends his public key to Alice

        // Alice
        val decodedBobPublicKey = kyber.publicKey.from(bytes = bobKeyPair.public.encoded) // receives Bob's public key
        val (aliceSymmetricKey, encapsulation) = decodedBobPublicKey.generateEncapsulatedAESKey() // generated encryption key with encapsulation
        // sends the encapsulation to Bob

        // Bob
        // receives the encapsulation from Alice
        val bobSymmetricKey = bobKeyPair.private.decryptEncapsulatedAESKey(encapsulation) // decrypts the encapsulated key

        // Alice
        // after the handshake is completed
        val aliceMessage = "a message".toByteArray() // prepares a message for Bob
        val encryptedByAlice = aliceSymmetricKey.ctr.encryptWithRandomIV(aliceMessage) // encrypts the message using the symmetric key and sends it to Bob

        // Bob
        // receives the encrypted data from Alice
        val decryptedByBobMessage = bobSymmetricKey.ctr.decrypt(encryptedByAlice) // decrypts the message
        assertThat(decryptedByBobMessage).isEqualTo(aliceMessage)

        assertThat(aliceSymmetricKey::encoded).isEqualTo(bobSymmetricKey.encoded)
        assertThat(aliceSymmetricKey::algorithm).isEqualTo(AES.name)
        assertThat(aliceSymmetricKey).isEqualTo(bobSymmetricKey)
    }

    @Test
    fun `sending Kyber keys over the wire - 1024`() {

        val keyPair = kyber.keyPair(arguments = Kyber.KeyPairArguments(variant = KYBER_1024))

        val decodedPublicKey = kyber.publicKey.from(bytes = keyPair.public.encoded)
        val decodedPrivateKey = kyber.privateKey.from(bytes = keyPair.private.encoded)

        assertThat(keyPair.private::algorithm).isEqualTo(KYBER_1024.algorithmName)
        assertThat(keyPair.public::algorithm).isEqualTo(KYBER_1024.algorithmName)
        assertThat(decodedPrivateKey).isEqualTo(keyPair.private)
        assertThat(decodedPublicKey).isEqualTo(keyPair.public)
    }

    @Test
    fun `sending Kyber keys over the wire - 768`() {

        val keyPair = kyber.keyPair(arguments = Kyber.KeyPairArguments(variant = KYBER_768))

        val decodedPublicKey = kyber.publicKey.from(bytes = keyPair.public.encoded)
        val decodedPrivateKey = kyber.privateKey.from(bytes = keyPair.private.encoded)

        assertThat(keyPair.private::algorithm).isEqualTo(KYBER_768.algorithmName)
        assertThat(keyPair.public::algorithm).isEqualTo(KYBER_768.algorithmName)
        assertThat(decodedPrivateKey).isEqualTo(keyPair.private)
        assertThat(decodedPublicKey).isEqualTo(keyPair.public)
    }

    @Test
    fun `sending Kyber keys over the wire - 512`() {

        val keyPair = kyber.keyPair(arguments = Kyber.KeyPairArguments(variant = KYBER_512))

        val decodedPublicKey = kyber.publicKey.from(bytes = keyPair.public.encoded)
        val decodedPrivateKey = kyber.privateKey.from(bytes = keyPair.private.encoded)

        assertThat(keyPair.private::algorithm).isEqualTo(KYBER_512.algorithmName)
        assertThat(keyPair.public::algorithm).isEqualTo(KYBER_512.algorithmName)
        assertThat(decodedPrivateKey).isEqualTo(keyPair.private)
        assertThat(decodedPublicKey).isEqualTo(keyPair.public)
    }

    @Test
    fun `using Dilithium-5-AES to sign and verify`() {

        val keyPair = dilithium.keyPair(variant = DILITHIUM_5)
        val message = "something to attest".toByteArray()

        val signature = keyPair.private.sign(message)
        val verifies = keyPair.public.verify(message, signature)

        assertThat(verifies).isTrue()
        assertThat(signature.metadata::keyHash).isEqualTo(keyPair.private.hash)
        assertThat(signature.metadata::algorithmName).isEqualTo(keyPair.private.algorithm)

        val notTheOriginalSigner = dilithium.keyPair(variant = DILITHIUM_5).public

        assertThat(notTheOriginalSigner.verify(message, signature)).isFalse()
    }

    @Test
    fun `sending Dilithium keys over the wire`() {

        val keyPair = dilithium.keyPair(arguments = Dilithium.KeyPairArguments(variant = DILITHIUM_5))

        val decodedPublicKey = dilithium.publicKey.from(bytes = keyPair.public.encoded)
        val decodedPrivateKey = dilithium.privateKey.from(bytes = keyPair.private.encoded)

        assertThat(keyPair.private::algorithm).isEqualTo(DILITHIUM_5.value)
        assertThat(keyPair.public::algorithm).isEqualTo(DILITHIUM_5.value)
        assertThat(decodedPrivateKey).isEqualTo(keyPair.private)
        assertThat(decodedPublicKey).isEqualTo(keyPair.public)
    }

    @Test
    fun `encrypting and decrypting with AES-256`() {

        val message = "something secret".toByteArray()
        val secretKey = aes.key(variant = AES_256)
        val decodedKey = aes.key.from(bytes = secretKey.encoded)

        val encrypted = secretKey.ctr.encryptWithRandomIV(message)
        val decrypted = decodedKey.ctr.decrypt(encrypted)

        assertThat(decrypted).isEqualTo(message)
    }

    val cryptography: CryptographicOperations
    val kyber get() = cryptography.asymmetric.crystals.kyber
    val dilithium get() = cryptography.asymmetric.crystals.dilithium
    val aes get() = cryptography.symmetric.aes
}