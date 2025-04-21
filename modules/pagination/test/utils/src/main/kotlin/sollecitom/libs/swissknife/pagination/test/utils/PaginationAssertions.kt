package sollecitom.libs.swissknife.pagination.test.utils

import assertk.Assert
import assertk.assertions.isNull
import sollecitom.libs.swissknife.pagination.domain.Page

fun Assert<Page<*>>.hasNoNextPage() = given { page ->

    assertThat(page.information.continuationToken).isNull()
}