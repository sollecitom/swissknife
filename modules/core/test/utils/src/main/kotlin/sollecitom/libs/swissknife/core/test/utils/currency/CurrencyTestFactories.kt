package sollecitom.libs.swissknife.core.test.utils.currency

import sollecitom.libs.swissknife.core.domain.currency.Currency
import sollecitom.libs.swissknife.core.domain.currency.known.EUR
import sollecitom.libs.swissknife.core.domain.currency.known.GBP
import sollecitom.libs.swissknife.core.domain.currency.known.JPY
import sollecitom.libs.swissknife.core.domain.currency.known.USD
import sollecitom.libs.swissknife.core.utils.RandomGenerator

private val knownCurrencyValues = setOf(Currency.USD, Currency.EUR, Currency.GBP, Currency.JPY)

val Currency.Companion.knownCurrencies: Set<Currency<*>> get() = knownCurrencyValues

context(generator: RandomGenerator)
fun Currency.Companion.random(): Currency<*> = knownCurrencies.random(generator.random)