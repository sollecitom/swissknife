package sollecitom.libs.swissknife.core.domain.identity

/** A simple string-based [Id]. Must be non-blank. */
@JvmInline
value class StringId(override val stringValue: String) : Id {

    init {
        require(stringValue.isNotBlank()) { "ID value cannot be blank" }
    }
}