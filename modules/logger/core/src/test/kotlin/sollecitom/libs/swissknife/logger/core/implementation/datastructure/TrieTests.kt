package sollecitom.libs.swissknife.logger.core.implementation.datastructure

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

private class TrieTests {

    @Test
    fun `searchLongestPrefixWord returns the longest stored word that is a prefix of the input`() {

        val trie = mutableTrieOf(setOf("org.apache", "org.apache.pulsar"))

        assertThat(trie.searchLongestPrefixWord("org.apache.pulsar.client.impl.ConnectionPool")).isEqualTo("org.apache.pulsar")
        assertThat(trie.searchLongestPrefixWord("org.apache.http.Client")).isEqualTo("org.apache")
        assertThat(trie.searchLongestPrefixWord("com.example.Foo")).isEmpty()
    }

    @Test
    fun `searchLongestPrefixWord returns the inserted word when the input matches it exactly`() {

        val trie = mutableTrieOf(setOf("org.apache.pulsar"))

        assertThat(trie.searchLongestPrefixWord("org.apache.pulsar")).isEqualTo("org.apache.pulsar")
    }
}
