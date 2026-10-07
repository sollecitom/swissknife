package sollecitom.libs.swissknife.core.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.kotlin.extensions.number.toByteArray
import org.http4k.config.Environment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.time.Clock
import kotlin.time.Instant

@TestInstance(PER_CLASS)
class CoreDataGeneratorProviderTests {

    private val fixedClock = object : Clock {
        override fun now() = Instant.parse("2026-10-07T12:00:00Z")
    }

    @Test
    fun `the same configured seed reproduces the same ids`() {

        val seed = 42L.toByteArray()

        val first = CoreDataGenerator.provider(environment = Environment.EMPTY, clock = fixedClock, randomSeed = seed).newId.let { listOf(it(), it.external()) }
        val second = CoreDataGenerator.provider(environment = Environment.EMPTY, clock = fixedClock, randomSeed = seed).newId.let { listOf(it(), it.external()) }

        assertThat(first).isEqualTo(second)
    }

    @Test
    fun `a seed configured in the environment reproduces the same ids`() {

        val environment = Environment.from("random.seed" to "42")

        val first = CoreDataGenerator.provider(environment = environment, clock = fixedClock).newId()
        val second = CoreDataGenerator.provider(environment = environment, clock = fixedClock).newId()

        assertThat(first).isEqualTo(second)
    }

    @Test
    fun `without a configured seed ids are not reproducible`() {

        val first = CoreDataGenerator.provider(environment = Environment.EMPTY, clock = fixedClock).newId.let { listOf(it(), it.external()) }
        val second = CoreDataGenerator.provider(environment = Environment.EMPTY, clock = fixedClock).newId.let { listOf(it(), it.external()) }

        assertThat(first[0]).isNotEqualTo(second[0])
        assertThat(first[1]).isNotEqualTo(second[1])
    }
}
