package sollecitom.libs.swissknife.core.test.utils.file

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import sollecitom.libs.swissknife.core.domain.file.FileContent

fun Assert<FileContent>.isNotEmptyWithFormat(format: FileContent.Format) = given { actual ->

    assertThat(actual.format).isEqualTo(format)
    assertThat(actual.length).isGreaterThan(0)
}
