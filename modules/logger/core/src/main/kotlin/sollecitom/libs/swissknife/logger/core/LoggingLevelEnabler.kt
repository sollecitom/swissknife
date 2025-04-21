package sollecitom.libs.swissknife.logger.core

import sollecitom.libs.swissknife.logger.core.implementation.LongestPrefixMatchLoggingLevelEnabler

fun loggingLevelEnabler(defaultMinimumLoggingLevel: LoggingLevel, customize: LoggingLevelEnablerCustomizer.() -> Unit): (LoggingLevel, String) -> Boolean {

    val prefixMap = mutableMapOf<String, LoggingLevel>()
    val customizer = object : LoggingLevelEnablerCustomizer {
        override fun String.withMinimumLoggingLevel(level: LoggingLevel) {
            prefixMap[this] = level
        }
    }
    customize(customizer)
    return LongestPrefixMatchLoggingLevelEnabler(prefixMap, defaultMinimumLoggingLevel)
}

