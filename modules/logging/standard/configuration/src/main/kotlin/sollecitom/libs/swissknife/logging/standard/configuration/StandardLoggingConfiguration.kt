package sollecitom.libs.swissknife.logging.standard.configuration

import org.http4k.config.Environment
import org.http4k.config.MapEnvironment
import sollecitom.libs.swissknife.logger.core.*
import sollecitom.libs.swissknife.logger.core.appender.PrintStreamAppender
import sollecitom.libs.swissknife.logger.core.defaults.DefaultFormatToString
import sollecitom.libs.swissknife.logger.json.formatter.DefaultFormatToJson

object StandardLoggingConfiguration {

    operator fun invoke(
        environment: Environment,
        minimumLoggingLevelEnvironmentVariableName: String = Properties.LOGGING_LEVEL_ENV_VARIABLE,
        minimumLoggingLevelOverridesEnvironmentVariableName: String = Properties.LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE,
        logFormatEnvironmentVariableName: String = Properties.FORMAT_ENV_VARIABLE,
        defaultMinimumLoggingLevel: LoggingLevel = LoggingLevel.INFO,
        defaultMinimumLoggingLevelOverrides: Map<String, LoggingLevel> = emptyMap(),
        defaultLogFormat: LogFormat = LogFormat.PLAIN
    ) = invoke(
        minimumLoggingLevelEnvironmentVariableName,
        minimumLoggingLevelOverridesEnvironmentVariableName,
        logFormatEnvironmentVariableName,
        defaultMinimumLoggingLevel,
        defaultMinimumLoggingLevelOverrides,
        defaultLogFormat,
        environment::get
    )

    operator fun invoke(
        minimumLoggingLevelEnvironmentVariableName: String = Properties.LOGGING_LEVEL_ENV_VARIABLE,
        minimumLoggingLevelOverridesEnvironmentVariableName: String = Properties.LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE,
        logFormatEnvironmentVariableName: String = Properties.FORMAT_ENV_VARIABLE,
        defaultMinimumLoggingLevel: LoggingLevel = LoggingLevel.INFO,
        defaultMinimumLoggingLevelOverrides: Map<String, LoggingLevel> = emptyMap(),
        defaultLogFormat: LogFormat = LogFormat.PLAIN,
        readConfigurationValue: (String) -> String? = ::defaultReadConfigurationValue
    ): LoggingCustomizer {

        val minimumLoggingLevelValue = defaultMinimumLoggingLevelFromEnvironment(minimumLoggingLevelEnvironmentVariableName, readConfigurationValue) ?: defaultMinimumLoggingLevel
        val minimumLoggingLevelOverridesValue = minimumLoggingLevelOverridesFromEnvironment(minimumLoggingLevelOverridesEnvironmentVariableName, readConfigurationValue) ?: defaultMinimumLoggingLevelOverrides
        val logFormatValue = logFormatFromEnvironment(logFormatEnvironmentVariableName, readConfigurationValue) ?: defaultLogFormat

        return CombinedLoggingCustomizer(minimumLoggingLevelValue, minimumLoggingLevelOverridesValue, logFormatValue.asFormattingFunction())
    }

    private fun defaultMinimumLoggingLevelFromEnvironment(key: String, readConfigurationValue: (String) -> String?): LoggingLevel? = readConfigurationValue(key)?.uppercase()?.let(LoggingLevel::valueOf)

    private fun minimumLoggingLevelOverridesFromEnvironment(key: String, readConfigurationValue: (String) -> String?): Map<String, LoggingLevel>? = readConfigurationValue(key)?.split(",")?.map { it.split("=") }?.associate { it.first() to LoggingLevel.valueOf(it.last()) }

    private fun logFormatFromEnvironment(key: String, readConfigurationValue: (String) -> String?): LogFormat? = readConfigurationValue(key)?.lowercase()?.let(::parseLogFormat)

    private fun LogFormat.asFormattingFunction(): FormatLogEntry<String> = when (this) {
        LogFormat.PLAIN -> DefaultFormatToString
        LogFormat.JSON -> DefaultFormatToJson
    }

    private fun parseLogFormat(value: String): LogFormat = when (value) {
        Properties.FORMAT_PLAIN -> LogFormat.PLAIN
        Properties.FORMAT_JSON -> LogFormat.JSON
        else -> error("Unknown log format $value")
    }

    object Properties {
        const val FORMAT_PLAIN = "plain"
        const val FORMAT_JSON = "json"
        const val LOGGING_LEVEL_ENV_VARIABLE = "LOGGING_LEVEL_DEFAULT"
        const val LOGGING_LEVEL_OVERRIDES_ENV_VARIABLE = "LOGGING_LEVELS"
        const val FORMAT_ENV_VARIABLE = "LOGGING_FORMAT"
    }
}

private fun defaultReadConfigurationValue(key: String): String? {
    val environment = MapEnvironment.from(System.getProperties()) overrides MapEnvironment.from(System.getenv().toProperties())
    return environment[key]
}

private class CombinedLoggingCustomizer(override val minimumLoggingLevel: LoggingLevel, override val minimumLoggingLevelOverrides: Map<String, LoggingLevel>, override val format: FormatLogEntry<String>) : LoggingCustomizer {

    override fun invoke(customizer: LoggerFactory.Customizer) {
        with(customizer) {
            loggingFunction = loggingFunction {
                addAppender(PrintStreamAppender(maximumLevel = LoggingLevel.WARN, stream = System::out, format = format))
                addAppender(PrintStreamAppender(minimumLevel = LoggingLevel.ERROR, stream = System::err, format = format))
            }
            isEnabledForLoggerName = loggingLevelEnabler(defaultMinimumLoggingLevel = minimumLoggingLevel) {
                minimumLoggingLevelOverrides.forEach { (loggerName, minimumLoggingLevel) -> loggerName withMinimumLoggingLevel minimumLoggingLevel }
            }
        }
    }
}