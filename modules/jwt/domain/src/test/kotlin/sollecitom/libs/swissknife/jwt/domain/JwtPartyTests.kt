package sollecitom.libs.swissknife.jwt.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.security.KeyPairGenerator

@TestInstance(PER_CLASS)
class JwtPartyTests {

    @Test
    fun `creating a JwtParty from companion factory`() {

        val id = StringOrURI("test-party")
        val keyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()

        val party = JwtParty(id = id, publicKey = keyPair.public)

        assertThat(party.id).isEqualTo(id)
        assertThat(party.publicKey).isEqualTo(keyPair.public)
    }

    @Test
    fun `two parties with same id and key are equal`() {

        val id = StringOrURI("test-party")
        val keyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()

        val party1 = JwtParty(id = id, publicKey = keyPair.public)
        val party2 = JwtParty(id = id, publicKey = keyPair.public)

        assertThat(party1).isEqualTo(party2)
    }
}
