package sollecitom.libs.swissknife.logger.core

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class LoggingLevelEnablerTests {

    private val isEnabled = loggingLevelEnabler(defaultMinimumLoggingLevel = LoggingLevel.INFO) {
        "com.foo".withMinimumLoggingLevel(LoggingLevel.ERROR)
        "com.foo.bar".withMinimumLoggingLevel(LoggingLevel.DEBUG)
    }

    @Test
    fun `a configured name covers its sub-packages`() {

        assertThat(isEnabled(LoggingLevel.WARN, "com.foo.Thing")).isFalse()
    }

    @Test
    fun `the longest configured name wins`() {

        assertThat(isEnabled(LoggingLevel.DEBUG, "com.foo.bar.Thing")).isTrue()
    }

    @Test
    fun `a configured name does not cover a sibling sharing its characters`() {

        assertThat(isEnabled(LoggingLevel.WARN, "com.foobar.Thing")).isTrue()
    }

    @Test
    fun `unconfigured names use the default level`() {

        assertThat(isEnabled(LoggingLevel.DEBUG, "org.other.Thing")).isFalse()
    }
}
