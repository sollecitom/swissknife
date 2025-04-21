package sollecitom.libs.swissknife.core.test.utils.text

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.core.utils.strings
import sollecitom.libs.swissknife.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters
import sollecitom.libs.swissknife.kotlin.extensions.text.strings

context(random: RandomGenerator)
fun Name.Companion.random(wordLengths: IntRange = 5..10, alphabet: CharRange = lowercaseCaseLetters): Name = random.strings(wordLengths, alphabet).iterator().next().let(::Name)

context(random: RandomGenerator)
fun Name.Companion.random(wordLength: Int, alphabet: CharRange = lowercaseCaseLetters): Name = random.strings(wordLength, alphabet).iterator().next().let(::Name)