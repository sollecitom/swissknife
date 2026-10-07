package sollecitom.libs.swissknife.core.domain.email

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isSuccess
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class EmailAddressTests {

    @Test
    fun `a well-formed email address is accepted`() {

        val text = "jane.doe@example.co.uk"

        val result = runCatching { EmailAddress(text) }

        assertThat(result).isSuccess().transform { it.value }.isEqualTo(text)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", "jane doe@example.com", "jane@doe@example.com", "@example.com", "jane@example", "jane@example.com.", "jane@.example.com", "jane@example..com"])
    fun `a malformed email address is rejected`(text: String) {

        val result = runCatching { EmailAddress(text) }

        assertThat(result).failedThrowing<IllegalArgumentException>()
    }
}
