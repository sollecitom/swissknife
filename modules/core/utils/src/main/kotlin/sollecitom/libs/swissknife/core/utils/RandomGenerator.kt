package sollecitom.libs.swissknife.core.utils

import sollecitom.libs.swissknife.kotlin.extensions.text.CharacterGroups.digitsAndLetters
import sollecitom.libs.swissknife.kotlin.extensions.text.strings
import java.security.SecureRandom
import kotlin.random.Random
import kotlin.random.asKotlinRandom
import kotlin.random.nextInt

interface RandomGenerator {

    val random: Random
    val secureRandom: SecureRandom
}

fun RandomGenerator.nextInt(range: IntRange) = random.nextInt(range)
fun RandomGenerator.nextInt(from: Int, until: Int) = random.nextInt(from, until)
fun RandomGenerator.nextLong(from: Long, until: Long) = random.nextLong(from, until)

fun RandomGenerator.string(wordLength: Int, alphabet: Iterable<Char> = digitsAndLetters): String = random.strings(wordLength..wordLength, alphabet).iterator().next()
fun RandomGenerator.string(wordLengths: IntRange, alphabet: Iterable<Char> = digitsAndLetters): String = random.strings(wordLengths, alphabet).iterator().next()
fun RandomGenerator.strings(wordLength: Int, alphabet: Iterable<Char> = digitsAndLetters) = random.strings(wordLength, alphabet)
fun RandomGenerator.strings(wordLengths: IntRange, alphabet: Iterable<Char> = digitsAndLetters) = random.strings(wordLengths, alphabet)

private class SecureRandomGeneratorAdapter(override val secureRandom: SecureRandom) : RandomGenerator {

    override val random = secureRandom.asKotlinRandom()
}

fun SecureRandom.asRandomGenerator(): RandomGenerator = SecureRandomGeneratorAdapter(secureRandom = this)