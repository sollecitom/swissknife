@file:Suppress("UNCHECKED_CAST")

package sollecitom.libs.swissknife.core.domain.currency.known

import sollecitom.libs.swissknife.core.domain.currency.Currency
import sollecitom.libs.swissknife.core.domain.currency.SpecificCurrencyAmount
import sollecitom.libs.swissknife.kotlin.extensions.number.withPrecision
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.reflect.KClass

val <CURRENCY : SpecificCurrencyAmount<CURRENCY>> KClass<CURRENCY>.currency: Currency<CURRENCY>
    get() = when (this) {
        Dollars::class -> Currency.USD as Currency<CURRENCY>
        Euros::class -> Currency.EUR as Currency<CURRENCY>
        Pounds::class -> Currency.GBP as Currency<CURRENCY>
        Yen::class -> Currency.JPY as Currency<CURRENCY>
        else -> error("Unknown currency for currency amount class $this")
    }

fun <CURRENCY : SpecificCurrencyAmount<CURRENCY>> BigDecimal.toCurrencyAmount(currency: Currency<CURRENCY>): CURRENCY {

    val rounded = withPrecision(currency.fractionalDigits.value)
    return when (currency) {
        Currency.USD -> Dollars(rounded) as CURRENCY
        Currency.EUR -> Euros(rounded) as CURRENCY
        Currency.GBP -> Pounds(rounded) as CURRENCY
        Currency.JPY -> Yen(rounded) as CURRENCY
        else -> error("Unsupported currency $currency")
    }
}

internal fun Number.toDecimal(): BigDecimal = when (this) {
    is BigDecimal -> this
    is BigInteger -> toBigDecimal()
    is Long, is Int, is Short, is Byte -> toLong().toBigDecimal()
    is Float -> toString().toBigDecimal()
    else -> toDouble().toBigDecimal()
}
