package sollecitom.libs.swissknife.logger.core.implementation

import sollecitom.libs.swissknife.logger.core.Log
import sollecitom.libs.swissknife.logger.core.LogEntry
import sollecitom.libs.swissknife.logger.core.Logger
import sollecitom.libs.swissknife.logger.core.LoggingContext
import org.slf4j.MDC
import java.time.Instant

internal class FunctionalLogger(override val name: String, override val isEnabledForLoggerName: sollecitom.libs.swissknife.logger.core.LoggingLevel.(name: String) -> Boolean, private val timeNow: () -> Instant, private val log: Log) : Logger {

    override fun log(level: sollecitom.libs.swissknife.logger.core.LoggingLevel, error: Throwable?, evaluateMessage: () -> String) {

        if (level.isEnabledForLoggerName(name)) {
            val message = evaluateMessage()
            val timestamp = timeNow()
            val context = context()
            val threadName = Thread.currentThread().name
            val entry = LogEntry(name, message, threadName, timestamp, error, level, context)
            log(entry)
        }
    }

    private fun context(): LoggingContext = ImmutableLoggingContext(MDC.getCopyOfContextMap() ?: emptyMap())
}