package sollecitom.libs.swissknife.logger.core

interface LoggingLevelEnablerCustomizer {

    infix fun String.withMinimumLoggingLevel(level: LoggingLevel)
}