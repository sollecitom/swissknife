package sollecitom.libs.swissknife.json.utils.checker.rules

internal fun List<String>.location(): String = if (isEmpty()) "" else " at ${joinToString(".")}"
