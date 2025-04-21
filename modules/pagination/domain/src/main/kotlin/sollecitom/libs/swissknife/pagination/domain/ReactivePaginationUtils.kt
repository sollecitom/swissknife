package sollecitom.libs.swissknife.pagination.domain

import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive

fun <ITEM> Pagination.asFlow(batchSize: Int, query: suspend (Pagination.Arguments) -> Page<ITEM>): Flow<ITEM> = flow {

    val firstPage = query(Pagination.Arguments(limit = batchSize))
    emitAll(firstPage.items.asFlow())
    var continuationToken = firstPage.information.continuationToken
    while (currentCoroutineContext().isActive && continuationToken != null) {
        val page = query(Pagination.Arguments(limit = batchSize, continuationToken = continuationToken))
        continuationToken = page.information.continuationToken
        emitAll(page.items.asFlow())
    }
}