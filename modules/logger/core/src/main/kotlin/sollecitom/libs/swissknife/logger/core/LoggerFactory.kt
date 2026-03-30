package sollecitom.libs.swissknife.logger.core

import java.time.Instant

/** Factory for creating [Logger] instances. Supports runtime reconfiguration via [configure]. */
interface LoggerFactory {

    val loggingFunction: Log
    val timeNow: () -> Instant
    val isEnabledForLoggerName: LoggingLevel.(name: String) -> Boolean

    fun configure(customize: Customizer.() -> Unit)

    fun forLoggable(loggable: Any): Logger

    fun logger(name: String): Logger

    interface Customizer {
        var loggingFunction: Log
        var timeNow: () -> Instant
        var isEnabledForLoggerName: LoggingLevel.(name: String) -> Boolean
    }
}