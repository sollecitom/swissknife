package sollecitom.libs.swissknife.sql.postgres.pagination.utils

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.pagination.domain.Page
import sollecitom.libs.swissknife.pagination.domain.Pagination
import sollecitom.libs.swissknife.pagination.domain.order.SortOrder
import sollecitom.libs.swissknife.sql.postgres.container.newPostgresContainer
import sollecitom.libs.swissknife.sql.reactive.utils.WithSqlConnectivity
import sollecitom.libs.swissknife.sql.reactive.utils.createR2dbcClient
import sollecitom.libs.swissknife.sql.reactive.utils.getValue
import sollecitom.libs.swissknife.sql.reactive.utils.nullOf
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import sollecitom.libs.swissknife.test.utils.execution.utils.test
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.r2dbc.core.await
import org.springframework.r2dbc.core.awaitSingle

@TestInstance(PER_CLASS)
class SelectPageTests : WithSqlConnectivity {

    private val postgres = newPostgresContainer().apply { start() }
    override val sqlClient = postgres.connectionOptions().createR2dbcClient()

    @BeforeAll
    fun createRows() = test {
        "CREATE TABLE items (pk BIGSERIAL PRIMARY KEY, label VARCHAR(20) NOT NULL, rating INT)".execute().await()
        (1..5).forEach { index -> "INSERT INTO items (label) VALUES (:label)".execute("label" to "item-$index").await() }
    }

    @Test
    fun `pages through every row with continuation tokens`() = test {

        val first = selectItems(Pagination.Arguments(limit = 2))
        val second = selectItems(Pagination.Arguments(limit = 2, continuationToken = first.information.continuationToken))
        val third = selectItems(Pagination.Arguments(limit = 2, continuationToken = second.information.continuationToken))

        assertThat(first.items + second.items + third.items).containsExactly("item-1", "item-2", "item-3", "item-4", "item-5")
        assertThat(first.information.totalItemsCount).isEqualTo(5L)
        assertThat(third.information.continuationToken).isNull()
    }

    @Test
    fun `a typed null binds against a non-text column`() = test {

        val count = "SELECT COUNT(*) AS n FROM items WHERE rating IS NOT DISTINCT FROM :rating".execute("rating" to nullOf<Int>()).map { row -> row.getValue<Long>("n") }.awaitSingle()

        assertThat(count).isEqualTo(5L)
    }

    @Test
    fun `a bare null is rejected`() = test {

        val result = runCatching { "SELECT 1 FROM items WHERE rating = :rating".execute("rating" to null) }

        assertThat(result).failedThrowing<IllegalArgumentException>()
    }

    private suspend fun selectItems(pagination: Pagination.Arguments): Page<String> = with(ItemsFilter) {
        "items".selectPage(pagination, sortOrder = null, bindings = emptySet()) { row, _ -> Entity(row.getValue<Long>("pk"), row.getValue<String>("label")) }
    }

    private object ItemsFilter : SqlEntityFilter<String> {

        override fun SortOrder?.orderByClause() = "pk"

        override fun Name?.whereClause(sortOrder: SortOrder?) = this?.let { "WHERE pk > :token" } ?: ""

        override fun Name?.whereQueryBindings(): Array<Pair<String, Any?>> = this?.let { arrayOf("token" to it.value.toLong()) } ?: emptyArray()

        override fun List<Entity<String>>.continuationToken(count: Long, pagination: Pagination.Arguments): Name? = takeIf { size == pagination.limit }?.lastOrNull()?.primaryKey?.toString()?.let(::Name)
    }

    @AfterAll
    fun stopPostgres() = postgres.stop()
}
