package sollecitom.libs.swissknife.core.utils

import sollecitom.libs.swissknife.configuration.utils.StandardEnvironment
import sollecitom.libs.swissknife.configuration.utils.randomSeed
import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdFactory
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.kotlin.extensions.number.toByteArray
import sollecitom.libs.swissknife.kotlin.extensions.time.toJavaClock
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import kotlinx.datetime.Clock
import org.http4k.config.Environment
import org.http4k.config.EnvironmentKey
import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom
import java.time.Clock as JavaClock

internal class CoreDataGeneratorProvider(private val environment: Environment, initialisedClock: Clock? = null, randomSeed: ByteArray? = null) : Loggable(), CoreDataGenerator {

    override val secureRandom: SecureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM).apply { setSeed(randomSeed ?: initialiseSecureRandomSeed()) }
    override val random: Random = secureRandom.asKotlinRandom()
    override val clock: Clock = initialisedClock ?: initialiseClock()
    override val javaClock: JavaClock by lazy { clock.toJavaClock() }
    override val newId: UniqueIdFactory by lazy { UniqueIdFactory.invoke(random = random, clock = clock) }

    private fun SecureRandom.initialiseSecureRandomSeed(): ByteArray {

        logger.info { "Reading random seed from property ${EnvironmentKey.randomSeed.meta.name}" }
        val seed = EnvironmentKey.randomSeed(environment) ?: nextLong()
        logger.info { "Initialised random from seed: $seed" }
        return seed.toByteArray()
    }

    private fun initialiseClock(): Clock {

        val clock = Clock.System
        logger.info { "Initialised clock to be the system clock" }
        return clock
    }

    companion object {
        const val SECURE_RANDOM_ALGORITHM = "SHA1PRNG"
    }
}

fun CoreDataGenerator.Companion.provider(environment: Environment = StandardEnvironment(), clock: Clock? = null, randomSeed: ByteArray? = null): CoreDataGenerator = CoreDataGeneratorProvider(environment = environment, initialisedClock = clock, randomSeed = randomSeed)