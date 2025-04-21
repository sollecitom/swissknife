package sollecitom.libs.swissknife.core.domain.text

import java.util.*

@JvmInline
value class CodeName(val value: String) : Comparable<CodeName> {

    init {
        require(value.isNotBlank()) { "value cannot be blank" }
    }

    val length: Int get() = value.length

    fun uppercase(locale: Locale = Locale.ROOT) = CodeName(value.uppercase(locale))

    fun lowercase(locale: Locale = Locale.ROOT) = CodeName(value.lowercase(locale))

    fun capitalized(locale: Locale = Locale.getDefault()) = CodeName(value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() })

    override fun compareTo(other: CodeName) = value.compareTo(other.value)

    companion object
}