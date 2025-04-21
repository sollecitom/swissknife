package sollecitom.libs.swissknife.logger.core.implementation

import sollecitom.libs.swissknife.logger.core.LoggingContext

internal class ImmutableLoggingContext(private val entries: Map<String, String> = mutableMapOf()) : LoggingContext {

    override fun asMap() = entries

    override val keys: Set<String> get() = entries.keys

    override fun get(key: String) = entries[key]
}

fun LoggingContext.Companion.withEntries(entries: Map<String, String>): LoggingContext = ImmutableLoggingContext(entries)