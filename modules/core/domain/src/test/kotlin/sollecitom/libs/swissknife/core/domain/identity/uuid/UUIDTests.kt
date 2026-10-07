package sollecitom.libs.swissknife.core.domain.identity.uuid

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isLessThan
import assertk.assertions.isInstanceOf
import assertk.assertions.containsExactly
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.UUID
import sollecitom.libs.swissknife.core.domain.identity.UUIDv7
import sollecitom.libs.swissknife.core.domain.identity.fromTypedString
import sollecitom.libs.swissknife.core.domain.identity.toTypedString
import sollecitom.libs.swissknife.core.domain.identity.factory.Factory
import sollecitom.libs.swissknife.core.domain.identity.utils.invoke
import sollecitom.libs.swissknife.kotlin.extensions.time.truncatedToMilliseconds
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.Duration.Companion.days
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.util.UUID as JavaUUID

@TestInstance(PER_CLASS)
class UUIDTests {

    private val factory = Id.Factory.invoke()

    @Test
    fun `parseOrNull returns a UUID for a valid string`() {

        val text = "550e8400-e29b-41d4-a716-446655440000"

        val id = UUID.parseOrNull(text)

        assertThat(id).isNotNull()
    }

    @Test
    fun `parseOrNull returns null for an invalid string`() {

        val text = "not-a-uuid"

        val id = UUID.parseOrNull(text)

        assertThat(id).isNull()
    }

    @Test
    fun `parseHexDashOrNull accepts hex-and-dash and rejects plain hexadecimal`() {

        val hexDash = "550e8400-e29b-41d4-a716-446655440000"
        val plainHex = "550e8400e29b41d4a716446655440000"

        val fromHexDash = UUID.parseHexDashOrNull(hexDash)
        val fromPlainHex = UUID.parseHexDashOrNull(plainHex)

        assertThat(fromHexDash).isNotNull()
        assertThat(fromPlainHex).isNull()
    }

    @Test
    fun `parseHexOrNull accepts plain hexadecimal and rejects hex-and-dash`() {

        val plainHex = "550e8400e29b41d4a716446655440000"
        val hexDash = "550e8400-e29b-41d4-a716-446655440000"

        val fromPlainHex = UUID.parseHexOrNull(plainHex)
        val fromHexDash = UUID.parseHexOrNull(hexDash)

        assertThat(fromPlainHex).isNotNull()
        assertThat(fromHexDash).isNull()
    }

    @Test
    fun `parsing round-trips through the string value`() {

        val text = "550e8400-e29b-41d4-a716-446655440000"

        val id = UUID.parseOrNull(text)

        assertThat(id?.stringValue).isEqualTo(text)
    }

    @Test
    fun `generating v4 UUIDs`() {

        val id = factory.uuid.v4()

        assertThat(JavaUUID.fromString(id.stringValue).version()).isEqualTo(4)
    }

    @Test
    fun `generating v7 UUIDs`() {

        val id = factory.uuid.v7()

        assertThat(JavaUUID.fromString(id.stringValue).version()).isEqualTo(7)
    }

    @Test
    fun `a v7 UUID carries the timestamp it was generated for`() {

        val timestamp = Clock.System.now()

        val id = factory.uuid.v7(timestamp)

        assertThat(id.timestamp).isEqualTo(timestamp.truncatedToMilliseconds())
    }

    @Test
    fun `v7 UUIDs sort by their timestamp`() {

        val earlierTimestamp = Clock.System.now()
        val laterTimestamp = earlierTimestamp + 10.days

        val earlier = factory.uuid.v7(earlierTimestamp)
        val later = factory.uuid.v7(laterTimestamp)

        assertThat(earlier).isLessThan(later)
    }

    @Test
    fun `the default internal id is a time-ordered v7 UUID`() {

        val id = factory.internal()

        assertThat(id).isInstanceOf(UUIDv7::class)
        assertThat(JavaUUID.fromString(id.stringValue).version()).isEqualTo(7)
    }

    @Test
    fun `the typed string form round-trips a v7 UUID to the sortable UUIDv7 type`() {

        val original = factory.uuid.v7()

        val id = original.toTypedString().let(Id::fromTypedString)

        assertThat(id).isEqualTo(original)
    }

    @Test
    fun `v7 UUIDs honour the injected random and clock`() {

        val now = Instant.parse("2026-01-01T00:00:00Z")
        val clock = object : Clock { override fun now() = now }

        val first = Id.Factory.invoke(random = Random(42), clock = clock).uuid.v7()
        val second = Id.Factory.invoke(random = Random(42), clock = clock).uuid.v7()

        assertThat(first).isEqualTo(second)
        assertThat(first.timestamp).isEqualTo(now)
    }

    @Test
    fun `v7 UUIDs stay monotonic when the clock does not move`() {

        val now = Instant.parse("2026-01-01T00:00:00Z")
        val factory = Id.Factory.invoke(clock = object : Clock { override fun now() = now })

        val ids = (1..1000).map { factory.uuid.v7() }

        assertThat(ids).containsExactly(*ids.sorted().toTypedArray())
        assertThat(ids.toSet().size).isEqualTo(ids.size)
    }

    @Test
    fun `v4 UUIDs honour the injected random`() {

        val first = Id.Factory.invoke(random = Random(42)).uuid.v4()
        val second = Id.Factory.invoke(random = Random(42)).uuid.v4()

        assertThat(first).isEqualTo(second)
        assertThat(JavaUUID.fromString(first.stringValue).version()).isEqualTo(4)
    }

    @Test
    fun `a UUID of another version is not accepted as a v7 UUID`() {

        val v4Text = "550e8400-e29b-41d4-a716-446655440000"

        val result = runCatching { UUIDv7(v4Text) }

        assertThat(result).isFailure().isInstanceOf(IllegalArgumentException::class)
    }
}
