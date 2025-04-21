package sollecitom.libs.swissknife.pagination.test.utils

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.core.utils.nextInt
import sollecitom.libs.swissknife.core.utils.nextLong
import sollecitom.libs.swissknife.pagination.domain.Pagination
import kotlin.random.nextInt

context(random: RandomGenerator, ids: UniqueIdGenerator)
fun Pagination.Arguments.Companion.create(limit: Int = random.nextInt(1..50), continuationToken: String? = ids.newId.external().stringValue) = Pagination.Arguments(limit = limit, continuationToken = continuationToken?.let(::Name))

context(random: RandomGenerator, ids: UniqueIdGenerator)
fun Pagination.Information.Companion.create(totalElementsCount: Long = random.nextLong(1, 100_000), continuationToken: String? = ids.newId.external().stringValue) = Pagination.Information(totalItemsCount = totalElementsCount, continuationToken = continuationToken?.let(::Name))