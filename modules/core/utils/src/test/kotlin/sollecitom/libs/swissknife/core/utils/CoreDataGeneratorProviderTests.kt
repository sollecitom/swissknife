package sollecitom.libs.swissknife.core.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import sollecitom.libs.swissknife.kotlin.extensions.number.toByteArray
import org.http4k.config.Environment
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class CoreDataGeneratorProviderTests {

    @Test
    fun `the same seed reproduces the same random values`() {

        val seed = 42L.toByteArray()

        val first = CoreDataGenerator.provider(environment = Environment.EMPTY, randomSeed = seed).random.nextLong()
        val second = CoreDataGenerator.provider(environment = Environment.EMPTY, randomSeed = seed).random.nextLong()

        assertThat(first).isEqualTo(second)
    }

    @Test
    fun `without a configured seed the secure random is not reproducible`() {

        val first = CoreDataGenerator.provider(environment = Environment.EMPTY).secureRandom.nextLong()
        val second = CoreDataGenerator.provider(environment = Environment.EMPTY).secureRandom.nextLong()

        assertThat(first).isNotEqualTo(second)
    }
}
