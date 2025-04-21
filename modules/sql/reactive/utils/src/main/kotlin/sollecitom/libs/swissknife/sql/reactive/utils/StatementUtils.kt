package sollecitom.libs.swissknife.sql.reactive.utils

import io.r2dbc.spi.Connection
import io.r2dbc.spi.Statement
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle

inline fun <reified T : Any> Statement.bindNullable(index: Int, value: T?): Statement {

    if (value != null) return bind(index, value)
    return bindNull(index, T::class.java)
}

inline fun <reified T : Any> Statement.bindNullable(name: String, value: T?): Statement {

    if (value != null) return bind(name, value)
    return bindNull(name, T::class.javaObjectType)
}

inline fun <reified T : Any> Statement.bindNullable(binding: Pair<String, T?>) = bindNullable(binding.first, binding.second)
inline fun <reified T : Any> Statement.bind(binding: Pair<String, T>) = bindNullable(binding.first, binding.second)

inline fun <reified T : Any> Statement.bindIndexedNullable(binding: Pair<Int, T?>) = bindNullable(binding.first, binding.second)
inline fun <reified T : Any> Statement.bindIndexed(binding: Pair<Int, T>) = bindNullable(binding.first, binding.second)

suspend fun Statement.executeSingle(): Long = execute().awaitSingle().rowsUpdated.awaitSingle()
suspend fun Statement.executeMulti(): List<Long> = execute().asFlow().map { it.rowsUpdated.awaitSingle() }.toList()

context(connection: Connection)
fun String.asStatement(): Statement = connection.createStatement(trimMargin())

fun <VALUE : Any> Statement.bindEach(values: Collection<VALUE>, bind: Statement.(VALUE) -> Unit): Statement {

    values.forEachIndexed { index, value ->
        bind(value)
        if (index < values.size - 1) {
            add()
        }
    }
    return this
}