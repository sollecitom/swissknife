package sollecitom.libs.swissknife.core.utils

import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom

interface RandomGenerator {

    val random: Random
    val secureRandom: SecureRandom
}

private class SecureRandomGeneratorAdapter(override val secureRandom: SecureRandom) : RandomGenerator {

    override val random = secureRandom.asKotlinRandom()
}

fun SecureRandom.asRandomGenerator(): RandomGenerator = SecureRandomGeneratorAdapter(secureRandom = this)