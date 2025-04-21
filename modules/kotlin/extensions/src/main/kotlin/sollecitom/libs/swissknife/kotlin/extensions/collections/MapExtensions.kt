package sollecitom.libs.swissknife.kotlin.extensions.collections

fun Map<String, String?>.withKeysPrefix(prefix: String, separator: String = "."): Map<String, String?> = mapKeys { (key, _) -> "$prefix$separator$key" }

fun <KEY, VALUE> Map<KEY, VALUE>.toPairsArray(): Array<Pair<KEY, VALUE>> = entries.map(Map.Entry<KEY, VALUE>::toPair).toTypedArray()

fun <KEY, VALUE> Map<KEY, VALUE>.inverted(): Map<VALUE, KEY> = entries.associate { it.value to it.key }