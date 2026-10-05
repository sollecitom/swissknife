package sollecitom.libs.swissknife.kotlin.extensions.number

fun Int.pow(exponent: Int): Int = toBigInteger().pow(exponent).intValueExact()

fun Long.pow(exponent: Int): Long = toBigInteger().pow(exponent).longValueExact()
