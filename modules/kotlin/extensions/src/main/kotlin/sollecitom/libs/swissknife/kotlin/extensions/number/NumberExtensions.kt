package sollecitom.libs.swissknife.kotlin.extensions.number

fun Int.pow(exponent: Int): Int = Math.toIntExact(toLong().pow(exponent))

fun Long.pow(exponent: Int): Long {

    require(exponent >= 0) { "exponent must not be negative, but was $exponent" }
    var result = 1L
    var base = this
    var remainingExponent = exponent
    while (remainingExponent > 0) {
        if (remainingExponent and 1 == 1) result = Math.multiplyExact(result, base)
        remainingExponent = remainingExponent shr 1
        if (remainingExponent > 0) base = Math.multiplyExact(base, base)
    }
    return result
}
