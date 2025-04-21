package sollecitom.libs.swissknife.sql.postgres.pagination.utils

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.pagination.domain.Page
import sollecitom.libs.swissknife.pagination.domain.Pagination
import sollecitom.libs.swissknife.pagination.domain.order.SortOrder
import sollecitom.libs.swissknife.sql.reactive.utils.WithSqlConnectivity
import sollecitom.libs.swissknife.sql.reactive.utils.getValue
import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.r2dbc.core.awaitSingle
import org.springframework.r2dbc.core.flow

interface SqlEntityFilter<ENTITY : Any> {

    fun SortOrder?.orderByClause(): String

    fun Name?.whereClause(sortOrder: SortOrder?): String

    fun Name?.whereQueryBindings(): Array<Pair<String, Any?>>

    fun List<Entity<ENTITY>>.continuationToken(count: Long, pagination: Pagination.Arguments): Name?
}

context(filter: SqlEntityFilter<T>, connected: WithSqlConnectivity)
suspend fun <T : Any> String.selectPage(pagination: Pagination.Arguments, sortOrder: SortOrder?, bindings: Set<Pair<String, Any?>>, fields: String = "*", mapRow: (Row, RowMetadata) -> Entity<T>): Page<T> = with(filter) {
    with(connected) {
        coroutineScope {

            val count = async {
                """SELECT COUNT(*) as n_records FROM ${this@selectPage}"""
                    .execute(bindings.toList())
                    .map { row -> row.getValue<Long>("n_records") }
                    .awaitSingle()
            }
            val entities = """
            SELECT $fields FROM ${this@selectPage} ${pagination.continuationToken.whereClause(sortOrder)} ORDER BY ${sortOrder.orderByClause()} LIMIT ${pagination.limit}
        """
                .execute(listOf(*bindings.toTypedArray(), *pagination.continuationToken.whereQueryBindings()))
                .map { row, rowMetadata ->
                    mapRow(row, rowMetadata)
                }
                .flow().toList()

            val continuationToken = entities.continuationToken(count.await(), pagination)
            val pageInformation = Pagination.Information(count.await(), continuationToken)
            Page(items = entities.map { it.value }, information = pageInformation)
        }
    }
}