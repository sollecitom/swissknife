package sollecitom.libs.swissknife.core.domain.currency.known

import sollecitom.libs.swissknife.core.domain.currency.Currency
import sollecitom.libs.swissknife.core.domain.currency.JavaCurrencyAdapter
import sollecitom.libs.swissknife.core.domain.currency.SpecificCurrencyAmountTemplate
import sollecitom.libs.swissknife.core.domain.currency.toUnits
import java.math.BigDecimal
import java.math.BigInteger

private val eur: Currency<Euros> by lazy { JavaCurrencyAdapter(java.util.Currency.getInstance("EUR")) }
val Currency.Companion.EUR get() = eur

class Euros(units: BigInteger) : SpecificCurrencyAmountTemplate<Euros>(units, Currency.EUR, ::Euros) {

    constructor(decimalValue: BigDecimal) : this(decimalValue.toUnits(Currency.EUR))
}

val Number.euros: Euros get() = Euros(toDouble().toBigDecimal())
val Int.euroCents: Euros get() = Euros(toBigInteger())
val Long.euroCents: Euros get() = Euros(toBigInteger())