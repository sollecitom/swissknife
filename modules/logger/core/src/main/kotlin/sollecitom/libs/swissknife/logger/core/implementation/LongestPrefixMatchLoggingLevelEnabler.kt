package sollecitom.libs.swissknife.logger.core.implementation

internal class LongestPrefixMatchLoggingLevelEnabler(private val prefixMap: Map<String, sollecitom.libs.swissknife.logger.core.LoggingLevel>, private val defaultMinimumLoggingLevel: sollecitom.libs.swissknife.logger.core.LoggingLevel) : (sollecitom.libs.swissknife.logger.core.LoggingLevel, String) -> Boolean {

    private val trie: sollecitom.libs.swissknife.logger.core.implementation.datastructure.Trie = sollecitom.libs.swissknife.logger.core.implementation.datastructure.mutableTrieOf(prefixMap.keys)

    override fun invoke(level: sollecitom.libs.swissknife.logger.core.LoggingLevel, loggerName: String): Boolean {

        val longestPrefixMatch = trie.searchLongestPrefixWord(loggerName)
        val minimumLevel = prefixMap[longestPrefixMatch] ?: defaultMinimumLoggingLevel
        return level >= minimumLevel
    }
}