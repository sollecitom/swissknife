package sollecitom.libs.swissknife.logging.standard.configuration

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.logger.core.LoggingLevel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class LoggingLevelConfigurationTests {

    @Test
    fun `configured overrides are trimmed, case-insensitive, and merged over the defaults`() {

        val configuration = mapOf(StandardLoggingConfiguration.Properties.LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE to " com.foo = debug , org.eclipse.jetty=error ")
        val defaults = mapOf("org.eclipse.jetty" to LoggingLevel.WARN, "org.apache.hc" to LoggingLevel.WARN)

        val customizer = StandardLoggingConfiguration(defaultMinimumLoggingLevelOverrides = defaults, readConfigurationValue = configuration::get)

        assertThat(customizer.minimumLoggingLevelOverrides).isEqualTo(mapOf("org.eclipse.jetty" to LoggingLevel.ERROR, "org.apache.hc" to LoggingLevel.WARN, "com.foo" to LoggingLevel.DEBUG))
    }

    @Test
    fun `the default level is trimmed and case-insensitive`() {

        val configuration = mapOf(StandardLoggingConfiguration.Properties.LOGGING_LEVEL_ENV_VARIABLE to " warn ")

        val customizer = StandardLoggingConfiguration(readConfigurationValue = configuration::get)

        assertThat(customizer.minimumLoggingLevel).isEqualTo(LoggingLevel.WARN)
    }
}
