package sollecitom.libs.swissknife.configuration.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.messageContains
import org.http4k.config.Environment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing

@TestInstance(PER_CLASS)
class PropertiesUnderRootTests {

    private val knownNames = setOf("operationTimeoutMs", "statsIntervalSeconds")

    @Test
    fun `properties are keyed by their known name however they are spelled`() {

        val environment = Environment.from("pulsar.configuration.operationTimeoutMs" to "5000", "PULSAR_CONFIGURATION_STATS_INTERVAL_SECONDS" to "7")

        val properties = environment.configurationPropertiesUnderRoot(root = "pulsar.configuration", knownNames = knownNames)

        assertThat(properties).isEqualTo(mapOf("operationTimeoutMs" to "5000", "statsIntervalSeconds" to "7"))
    }

    @Test
    fun `properties outside the root are ignored`() {

        val environment = Environment.from("pulsar.other.operationTimeoutMs" to "5000", "service.port" to "8080")

        val properties = environment.configurationPropertiesUnderRoot(root = "pulsar.configuration", knownNames = knownNames)

        assertThat(properties).isEqualTo(emptyMap())
    }

    @Test
    fun `an unknown property under the root is an error`() {

        val environment = Environment.from("pulsar.configuration.operationTimeoutMillis" to "5000")

        val result = runCatching { environment.configurationPropertiesUnderRoot(root = "pulsar.configuration", knownNames = knownNames) }

        assertThat(result).failedThrowing<IllegalArgumentException>().messageContains("Unknown configuration properties under 'pulsar.configuration': [operationtimeoutmillis]")
    }

    @Test
    fun `known names that can't be told apart are an error`() {

        val result = runCatching { Environment.EMPTY.configurationPropertiesUnderRoot(root = "pulsar.configuration", knownNames = setOf("useTls", "use_tls")) }

        assertThat(result).failedThrowing<IllegalArgumentException>().messageContains("[useTls, use_tls] can't be told apart")
    }
}
