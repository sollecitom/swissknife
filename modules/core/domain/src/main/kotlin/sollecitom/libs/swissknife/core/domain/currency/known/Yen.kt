package sollecitom.libs.swissknife.core.domain.currency.known

import sollecitom.libs.swissknife.core.domain.currency.Currency
import sollecitom.libs.swissknife.core.domain.currency.JavaCurrencyAdapter
import sollecitom.libs.swissknife.core.domain.currency.SpecificCurrencyAmountTemplate
import sollecitom.libs.swissknife.core.domain.currency.toUnits
import java.math.BigDecimal
import java.math.BigInteger

private val jpy: Currency<Yen> by lazy { JavaCurrencyAdapter(java.util.Currency.getInstance("JPY")) }
val Currency.Companion.JPY get() = jpy

class Yen(units: BigInteger) : SpecificCurrencyAmountTemplate<Yen>(units, Currency.JPY, ::Yen) {

    constructor(decimalValue: BigDecimal) : this(decimalValue.toUnits(Currency.JPY))
}

val Int.yen: Yen get() = Yen(toBigInteger())
val Long.yen: Yen get() = Yen(toBigInteger())