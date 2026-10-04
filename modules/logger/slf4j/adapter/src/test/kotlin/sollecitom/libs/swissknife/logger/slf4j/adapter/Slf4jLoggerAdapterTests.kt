package sollecitom.libs.swissknife.logger.slf4j.adapter

import assertk.assertThat
import assertk.assertions.containsExactly
import sollecitom.libs.swissknife.logger.core.Logger
import sollecitom.libs.swissknife.logger.core.LoggingLevel
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class Slf4jLoggerAdapterTests {

    private val error = IllegalStateException("Boom")

    @Test
    fun `a trailing throwable after a single argument is logged as the error`() {

        val logger = RecordingLogger()

        logger.asSlf4jLogger().error("Failed for {}", 42, error)

        assertThat(logger.entries).containsExactly(Entry(LoggingLevel.ERROR, error, "Failed for 42"))
    }

    @Test
    fun `a throwable as the only argument is logged as the error`() {

        val logger = RecordingLogger()
        val argument: Any = error

        logger.asSlf4jLogger().warn("Failed for {}", argument)

        assertThat(logger.entries).containsExactly(Entry(LoggingLevel.WARN, error, "Failed for {}"))
    }

    @Test
    fun `a trailing throwable after several arguments is logged as the error`() {

        val logger = RecordingLogger()

        logger.asSlf4jLogger().info("Failed for {} and {}", 1, 2, error)

        assertThat(logger.entries).containsExactly(Entry(LoggingLevel.INFO, error, "Failed for 1 and 2"))
    }

    @Test
    fun `arguments without a throwable are logged without an error`() {

        val logger = RecordingLogger()

        logger.asSlf4jLogger().debug("Processed {} and {}", 1, 2)

        assertThat(logger.entries).containsExactly(Entry(LoggingLevel.DEBUG, null, "Processed 1 and 2"))
    }

    private data class Entry(val level: LoggingLevel, val error: Throwable?, val message: String)

    private class RecordingLogger : Logger {

        val entries = mutableListOf<Entry>()

        override val name = "recording"

        override fun log(level: LoggingLevel, error: Throwable?, evaluateMessage: () -> String) {
            entries += Entry(level, error, evaluateMessage())
        }

        override val isEnabledForLoggerName: LoggingLevel.(name: String) -> Boolean = { true }
    }
}
