package sollecitom.libs.swissknife.logging.standard.configuration

import sollecitom.libs.swissknife.logger.core.FormatLogEntry
import sollecitom.libs.swissknife.logger.core.Logger
import sollecitom.libs.swissknife.logger.core.loggable.Loggable

interface LoggingCustomizer : (sollecitom.libs.swissknife.logger.core.LoggerFactory.Customizer) -> Unit {

    val minimumLoggingLevel: sollecitom.libs.swissknife.logger.core.LoggingLevel
    val format: FormatLogEntry<String>
    val minimumLoggingLevelOverrides: Map<String, sollecitom.libs.swissknife.logger.core.LoggingLevel>

    fun logSettings(logger: Logger = Companion.logger) = logger.info { "Logging settings are 'minimumLoggingLevel': $minimumLoggingLevel, 'format': $format, 'minimumLoggingLevelOverrides': $minimumLoggingLevelOverrides" }

    companion object : Loggable()
}

fun LoggingCustomizer.applyTo(loggerFactory: sollecitom.libs.swissknife.logger.core.LoggerFactory) = loggerFactory.configure(this)