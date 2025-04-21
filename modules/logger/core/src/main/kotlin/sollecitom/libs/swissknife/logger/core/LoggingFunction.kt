package sollecitom.libs.swissknife.logger.core

fun loggingFunction(customize: LoggingFunctionCustomizer.() -> Unit): Log {

    val appenders = mutableSetOf<sollecitom.libs.swissknife.logger.core.appender.AppendLogEntry>()
    val customizer = object : LoggingFunctionCustomizer {
        override fun addAppender(appender: sollecitom.libs.swissknife.logger.core.appender.AppendLogEntry) {
            appenders += appender
        }
    }
    customize(customizer)
    return DelegatingLoggingFunction(appenders)
}

interface LoggingFunctionCustomizer {

    fun addAppender(appender: sollecitom.libs.swissknife.logger.core.appender.AppendLogEntry)
}

internal class DelegatingLoggingFunction(private val appenders: Set<sollecitom.libs.swissknife.logger.core.appender.AppendLogEntry>) : Log {

    override fun invoke(entry: LogEntry) {
        appenders.forEach { append -> append(entry) }
    }
}