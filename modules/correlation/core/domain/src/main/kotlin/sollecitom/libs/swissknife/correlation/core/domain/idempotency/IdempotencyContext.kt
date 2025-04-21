package sollecitom.libs.swissknife.correlation.core.domain.idempotency

import sollecitom.libs.swissknife.core.domain.text.Name

data class IdempotencyContext(val namespace: Name?, val key: Name) {

    fun id(separator: String = DEFAULT_IDEMPOTENCY_ID_SEGMENTS_SEPARATOR): Name = namespace?.let { "${it.value}${separator}${key.value}".let(::Name) } ?: key

    companion object {

        fun combinedNamespace(firstPart: String, vararg parts: String): Name = listOf(firstPart, *parts).joinToString(NAMESPACE_SEPARATOR).let(::Name)

        const val DEFAULT_IDEMPOTENCY_ID_SEGMENTS_SEPARATOR = "-"
        const val NAMESPACE_SEPARATOR = "-"
    }
}