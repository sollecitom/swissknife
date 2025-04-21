package sollecitom.libs.swissknife.core.test.utils.email

import sollecitom.libs.swissknife.core.domain.email.EmailAddress
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters

context(_: RandomGenerator)
fun EmailAddress.Companion.create(prefix: Name = Name.random(), domain: Name = "${Name.random(wordLengths = 4..8, alphabet = lowercaseCaseLetters).value}.com".let(::Name)) = EmailAddress("${prefix.value}@${domain.value}.")