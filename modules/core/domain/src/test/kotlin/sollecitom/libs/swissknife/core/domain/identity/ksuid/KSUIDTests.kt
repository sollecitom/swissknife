package sollecitom.libs.swissknife.core.domain.identity.ksuid

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.factory.Factory
import sollecitom.libs.swissknife.core.domain.identity.utils.invoke
import sollecitom.libs.swissknife.kotlin.extensions.time.fixed
import sollecitom.libs.swissknife.kotlin.extensions.time.truncatedToSeconds
import kotlin.time.Clock
import kotlin.time.Instant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.time.Duration.Companion.days

@TestInstance(PER_CLASS)
class KSUIDTests {

    @Test
    fun `generating KSUIDs`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)

        val id = Id.Factory.invoke(clock = clock).ksuid.monotonic()

        assertThat(id.timestamp).isEqualTo(timestamp.truncatedToSeconds())
    }

    @Test
    fun `generating a KSUID in the past`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)
        val pastTimestamp = timestamp - 10.days

        val id = Id.Factory.invoke(clock = clock).ksuid.monotonic(timestamp = pastTimestamp)

        assertThat(id.timestamp).isEqualTo(pastTimestamp.truncatedToSeconds())
    }

    @Test
    fun `generating a KSUID in the future`() {

        val timestamp = Clock.System.now()
        val clock = Clock.fixed(timestamp)
        val futureTimestamp = timestamp + 15.days

        val id = Id.Factory.invoke(clock = clock).ksuid.monotonic(timestamp = futureTimestamp)

        assertThat(id.timestamp).isEqualTo(futureTimestamp.truncatedToSeconds())
    }

    @Test
    fun `monotonic KSUIDs use the time of creation`() {

        var now = Instant.parse("2026-01-01T00:00:00Z")
        val clock = object : Clock {
            override fun now() = now
        }
        val factory = Id.Factory.invoke(clock = clock).ksuid.monotonic
        factory()
        now = Instant.parse("2026-01-01T01:00:00Z")

        val id = factory()

        assertThat(id.timestamp).isEqualTo(Instant.parse("2026-01-01T01:00:00Z"))
    }

    @Test
    fun `sub-second precision KSUIDs use the time of creation`() {

        var now = Instant.parse("2026-01-01T00:00:00Z")
        val clock = object : Clock {
            override fun now() = now
        }
        val factory = Id.Factory.invoke(clock = clock).ksuid.withSubSecondPrecision
        factory()
        now = Instant.parse("2026-01-01T01:00:00Z")

        val id = factory()

        assertThat(id.timestamp).isEqualTo(Instant.parse("2026-01-01T01:00:00Z"))
    }
}
