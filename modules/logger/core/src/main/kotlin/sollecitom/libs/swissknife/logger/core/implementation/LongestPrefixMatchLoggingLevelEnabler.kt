package sollecitom.libs.swissknife.logger.core.implementation

import sollecitom.libs.swissknife.logger.core.LoggingLevel

internal class LongestPrefixMatchLoggingLevelEnabler(private val prefixMap: Map<String, LoggingLevel>, private val defaultMinimumLoggingLevel: LoggingLevel) : (LoggingLevel, String) -> Boolean {

    override fun invoke(level: LoggingLevel, loggerName: String): Boolean {

        val minimumLevel = generateSequence(loggerName) { name -> name.substringBeforeLast(SEGMENT_SEPARATOR, missingDelimiterValue = "").ifEmpty { null } }.firstNotNullOfOrNull(prefixMap::get) ?: defaultMinimumLoggingLevel
        return level >= minimumLevel
    }

    private companion object {
        const val SEGMENT_SEPARATOR = '.'
    }
}
