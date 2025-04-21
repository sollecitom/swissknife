package sollecitom.libs.swissknife.core.domain.currency

import sollecitom.libs.swissknife.core.domain.quantity.Count
import sollecitom.libs.swissknife.core.domain.text.Name
import java.util.*

interface Currency<AMOUNT : SpecificCurrencyAmount<AMOUNT>> {

    val textualCode: Name
    val numericCode: Name
    val fractionalDigits: Count

    fun displayName(locale: Locale = Locale.getDefault(Locale.Category.DISPLAY)): Name

    fun symbol(locale: Locale = Locale.getDefault(Locale.Category.DISPLAY)): Name

    companion object
}