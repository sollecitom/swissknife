package sollecitom.libs.swissknife.logging.standard.configuration

import sollecitom.libs.swissknife.logger.core.JvmLoggerFactory
import sollecitom.libs.swissknife.logger.core.Log
import sollecitom.libs.swissknife.logger.core.LoggingLevel
import org.http4k.config.Environment

fun configureLogging(defaultMinimumLoggingLevel: LoggingLevel = LoggingLevel.INFO, minimumLoggingLevelOverrides: Map<String, LoggingLevel> = defaultMinimumLoggingLevelOverrides) {

    StandardLoggingConfiguration(defaultMinimumLoggingLevel = defaultMinimumLoggingLevel, defaultMinimumLoggingLevelOverrides = minimumLoggingLevelOverrides).applyTo(JvmLoggerFactory)
}

fun configureLogging(environment: Environment) {

    StandardLoggingConfiguration(environment = environment, defaultMinimumLoggingLevelOverrides = defaultMinimumLoggingLevelOverrides).applyTo(JvmLoggerFactory)
}

fun configureLogging(log: Log) = JvmLoggerFactory.configure { loggingFunction = log }

private val defaultMinimumLoggingLevelOverrides = mapOf(
    "org.eclipse.jetty" to LoggingLevel.WARN,
    "org.apache.hc" to LoggingLevel.WARN,
    "org.apache.pulsar.client.impl" to LoggingLevel.ERROR
)