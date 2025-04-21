package sollecitom.libs.swissknife.core.domain.currency.known

import sollecitom.libs.swissknife.core.domain.currency.Currency
import sollecitom.libs.swissknife.core.domain.currency.JavaCurrencyAdapter
import sollecitom.libs.swissknife.core.domain.currency.SpecificCurrencyAmountTemplate
import sollecitom.libs.swissknife.core.domain.currency.toUnits
import java.math.BigDecimal
import java.math.BigInteger

private val usd: Currency<Dollars> by lazy { JavaCurrencyAdapter(java.util.Currency.getInstance("USD")) }
val Currency.Companion.USD get() = usd

class Dollars(units: BigInteger) : SpecificCurrencyAmountTemplate<Dollars>(units, Currency.USD, ::Dollars) {

    constructor(decimalValue: BigDecimal) : this(decimalValue.toUnits(Currency.USD))
}

val Number.dollars: Dollars get() = Dollars(toDouble().toBigDecimal())
val Int.cents: Dollars get() = Dollars(toBigInteger())
val Long.cents: Dollars get() = Dollars(toBigInteger())