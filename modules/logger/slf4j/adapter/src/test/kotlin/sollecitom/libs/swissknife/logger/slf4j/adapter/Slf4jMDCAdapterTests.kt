package sollecitom.libs.swissknife.logger.slf4j.adapter

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class Slf4jMDCAdapterTests {

    @Test
    fun `putting a null value removes the key`() {

        val adapter = Slf4jMDCAdapter()
        adapter.put("key", "value")

        adapter.put("key", null)

        assertThat(adapter.get("key")).isNull()
        assertThat(adapter.copyOfContextMap).isEmpty()
    }

    @Test
    fun `putting a null value for an absent key leaves the context empty`() {

        val adapter = Slf4jMDCAdapter()

        adapter.put("key", null)

        assertThat(adapter.copyOfContextMap).isEmpty()
    }

    @Test
    fun `a child thread doesn't inherit the parent's context`() {

        val adapter = Slf4jMDCAdapter()
        adapter.put("key", "value")
        var valueSeenByChild: String? = "unset"

        Thread { valueSeenByChild = adapter.get("key") }.apply { start(); join() }

        assertThat(valueSeenByChild).isNull()
    }
}
