package sollecitom.libs.swissknife.core.domain.identity

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.identity.factory.Factory
import sollecitom.libs.swissknife.core.domain.identity.utils.invoke
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class IdStringFormTests {

    private val factory = Id.Factory.invoke()

    @Test
    fun `fromString keeps the exact text as a StringId`() {

        val text = "01arz3ndektsv4rrffq69g5fav"

        val id = Id.fromString(text)

        assertThat(id).isEqualTo(StringId(text))
    }

    @Test
    fun `a StringId that looks like a UUID round-trips through fromString`() {

        val original = factory.external()

        val id = Id.fromString(original.stringValue)

        assertThat(id).isEqualTo(original)
    }

    @Test
    fun `the typed string form round-trips every ID type`() {

        val ids = listOf(factory.ulid.monotonic(), factory.ksuid.monotonic(), factory.uuid.v7(), factory.uuid.v4(), StringId("some:text"))

        val roundTripped = ids.map { Id.fromTypedString(it.toTypedString()) }

        assertThat(roundTripped).isEqualTo(ids)
    }

    @Test
    fun `the typed string form names the ID type`() {

        val id = StringId("abc")

        val typedString = id.toTypedString()

        assertThat(typedString).isEqualTo("string:abc")
    }

    @Test
    fun `an unknown type in the typed string form is rejected`() {

        val result = runCatching { Id.fromTypedString("snowflake:123") }

        assertThat(result).failedThrowing<IllegalArgumentException>()
    }
}
