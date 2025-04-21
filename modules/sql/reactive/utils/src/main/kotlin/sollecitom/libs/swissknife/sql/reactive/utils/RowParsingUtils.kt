package sollecitom.libs.swissknife.sql.reactive.utils

import io.r2dbc.spi.Readable

inline fun <reified TYPE : Any> Readable.getValueOrNull(columnName: String): TYPE? = get(columnName, TYPE::class.java)

inline fun <reified TYPE> Readable.cast(columnName: String) = get(columnName) as TYPE

fun Readable.booleanValue(columnName: String) = cast<Byte>(columnName).toInt() != 0

inline fun <reified TYPE : Any> Readable.getValue(key: String): TYPE = getValueOrNull(key)!!