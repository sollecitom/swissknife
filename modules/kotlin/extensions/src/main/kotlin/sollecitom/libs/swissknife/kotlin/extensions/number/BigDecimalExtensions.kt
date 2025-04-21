package sollecitom.libs.swissknife.kotlin.extensions.number

import sollecitom.libs.swissknife.kotlin.extensions.text.indexOfOrNull
import java.math.BigDecimal

fun BigDecimal.withPrecision(precision: Int): BigDecimal {
    val plain = movePointRight(precision).toPlainString()
    return BigDecimal(plain.substring(0, plain.indexOfOrNull(".") ?: plain.length)).movePointLeft(precision)
}