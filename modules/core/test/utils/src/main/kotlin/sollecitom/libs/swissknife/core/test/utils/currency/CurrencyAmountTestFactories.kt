package sollecitom.libs.swissknife.core.test.utils.currency

import sollecitom.libs.swissknife.core.domain.currency.Currency
import sollecitom.libs.swissknife.core.domain.currency.CurrencyAmount
import sollecitom.libs.swissknife.core.domain.currency.GenericCurrencyAmount
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.core.utils.nextLong

context(random: RandomGenerator)
fun CurrencyAmount.Companion.create(units: Long = random.nextLong(0L, 10_000_000L), currency: Currency<*> = Currency.random()): CurrencyAmount = GenericCurrencyAmount(units.toBigInteger(), currency)