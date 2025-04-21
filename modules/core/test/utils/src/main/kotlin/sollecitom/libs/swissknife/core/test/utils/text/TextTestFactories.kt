package sollecitom.libs.swissknife.core.test.utils.text

import sollecitom.libs.swissknife.core.domain.text.Text
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.core.utils.nextInt
import sollecitom.libs.swissknife.core.utils.strings
import sollecitom.libs.swissknife.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters
import sollecitom.libs.swissknife.kotlin.extensions.text.strings
import kotlin.random.nextInt

context(random: RandomGenerator)
fun Text.Companion.random(wordsCount: Int = random.nextInt(2..60), wordLengths: IntRange = 10..30, alphabet: CharRange = lowercaseCaseLetters): Text {

    return random.strings(wordLengths, alphabet).take(wordsCount).joinToString(separator = " ").let(::Text)
}