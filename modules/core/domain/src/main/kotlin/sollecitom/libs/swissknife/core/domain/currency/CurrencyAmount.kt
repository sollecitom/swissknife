package sollecitom.libs.swissknife.core.domain.currency

import java.math.BigDecimal
import java.math.BigInteger

/** A monetary amount in the smallest unit of a currency (e.g. cents). Supports arithmetic operations. */
interface CurrencyAmount {

    val units: BigInteger
    val currency: Currency<*>
    val decimalValue: BigDecimal

    fun withUnits(units: BigInteger): CurrencyAmount

    fun plusUnits(units: BigInteger): CurrencyAmount

    fun minusUnits(units: BigInteger): CurrencyAmount

    operator fun times(value: BigInteger): CurrencyAmount

    operator fun div(value: BigInteger): CurrencyAmount

    fun divAndRemainder(value: BigInteger): Pair<CurrencyAmount, CurrencyAmount>

    companion object
}