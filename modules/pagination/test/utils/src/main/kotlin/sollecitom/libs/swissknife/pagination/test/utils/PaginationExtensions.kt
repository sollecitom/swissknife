package sollecitom.libs.swissknife.pagination.test.utils

import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.pagination.domain.Page
import sollecitom.libs.swissknife.pagination.domain.Pagination

fun <ITEM> List<ITEM>.withPaginationInformation(information: Pagination.Information): Page<ITEM> = Page(items = this, information = information)

context(_: RandomGenerator, _: UniqueIdGenerator)
fun <ITEM> List<ITEM>.withPaginationInformation(): Page<ITEM> = withPaginationInformation(information = Pagination.Information.create())