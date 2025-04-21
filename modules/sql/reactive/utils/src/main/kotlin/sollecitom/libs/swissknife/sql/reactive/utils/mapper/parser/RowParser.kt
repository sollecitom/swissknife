package sollecitom.libs.swissknife.sql.reactive.utils.mapper.parser

import io.r2dbc.spi.Readable
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.RowsFetchSpec

fun interface RowParser<out VALUE : Any> {

    fun parse(row: Readable): VALUE
}

fun <VALUE : Any> DatabaseClient.GenericExecuteSpec.parseWith(parser: RowParser<VALUE>): RowsFetchSpec<VALUE> = map(parser::parse)