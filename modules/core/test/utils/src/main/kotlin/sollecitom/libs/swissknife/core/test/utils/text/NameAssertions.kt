package sollecitom.libs.swissknife.core.test.utils.text

import assertk.Assert
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.text.Name

fun Assert<Name>.hasValue(expectedValue: String) = given { actual ->

    assertThat(actual.value).isEqualTo(expectedValue)
}