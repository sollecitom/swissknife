package sollecitom.libs.swissknife.core.test.utils.currency

import assertk.Assert
import assertk.assertions.isNotZero
import assertk.assertions.isZero
import sollecitom.libs.swissknife.core.domain.currency.SpecificCurrencyAmount

fun <CURRENCY : SpecificCurrencyAmount<CURRENCY>> Assert<CURRENCY>.isZero() = given { amount ->

    assertThat(amount.units).isZero()
}

fun <CURRENCY : SpecificCurrencyAmount<CURRENCY>> Assert<CURRENCY>.isNotZero() = given { amount ->

    assertThat(amount.units).isNotZero()
}