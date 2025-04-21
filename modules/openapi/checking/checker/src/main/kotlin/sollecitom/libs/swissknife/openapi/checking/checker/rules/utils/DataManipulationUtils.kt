package sollecitom.libs.swissknife.openapi.checking.checker.rules.utils

internal fun Any.trimmed(): Any? = when (this) {
    is String -> takeIf { it.isNotBlank() }
    else -> this
}