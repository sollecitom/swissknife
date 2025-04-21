package sollecitom.libs.swissknife.core.domain.currency

import sollecitom.libs.swissknife.core.domain.quantity.Count
import sollecitom.libs.swissknife.core.domain.text.Name
import java.util.*
import java.util.Currency as JavaCurrency

@JvmInline
internal value class JavaCurrencyAdapter<AMOUNT : SpecificCurrencyAmount<AMOUNT>>(private val currency: JavaCurrency) : Currency<AMOUNT> {

    override val textualCode get() = currency.currencyCode.let(::Name)
    override val numericCode get() = currency.numericCodeAsString.let(::Name)
    override val fractionalDigits: Count get() = currency.defaultFractionDigits.let(::Count)

    override fun displayName(locale: Locale) = currency.getDisplayName(locale).let(::Name)

    override fun symbol(locale: Locale) = currency.getSymbol(locale).let(::Name)
}