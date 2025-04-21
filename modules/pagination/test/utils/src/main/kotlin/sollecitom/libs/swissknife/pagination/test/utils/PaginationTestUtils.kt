package sollecitom.libs.swissknife.pagination.test.utils

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.pagination.domain.Page
import sollecitom.libs.swissknife.pagination.domain.Pagination
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import sollecitom.libs.swissknife.core.utils.nextInt

context(random: RandomGenerator)
fun <T> pagesAsFlow(getPage: suspend (Pagination.Arguments) -> Page<T>): Flow<Pair<Page<T>, Pagination.Arguments>> = flow {

    var continuationToken: Name? = null
    while (currentCoroutineContext().isActive) {
        val limit = random.nextInt(1, 30)
        val arguments = Pagination.Arguments(limit = limit, continuationToken = continuationToken)
        val page = getPage(arguments)
        emit(page to arguments)
        continuationToken = page.information.continuationToken
        if (continuationToken == null) break
    }
}