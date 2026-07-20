package sollecitom.libs.swissknife.cryptography.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.mlkem.MLKEM
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.mlkem.MLKEM.Variant.*
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.kem.mlkem.invoke
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa.MLDSA
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa.MLDSA.Variant.ML_DSA_87
import sollecitom.libs.swissknife.cryptography.domain.asymmetric.signing.mldsa.invoke
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
    fun `encrypting and decrypting with AES-256`() {

        val message = "something secret".toByteArray()
        val secretKey = aes.key(variant = AES_256)
        val decodedKey = aes.key.from(bytes = secretKey.encoded)

        val encrypted = secretKey.ctr.encryptWithRandomIV(message)
        val decrypted = decodedKey.ctr.decrypt(encrypted)

        assertThat(decrypted).isEqualTo(message)
    }

    val cryptography: CryptographicOperations
    val mlKem get() = cryptography.asymmetric.nist.mlKem
    val mlDsa get() = cryptography.asymmetric.nist.mlDsa
    val aes get() = cryptography.symmetric.aes
}
