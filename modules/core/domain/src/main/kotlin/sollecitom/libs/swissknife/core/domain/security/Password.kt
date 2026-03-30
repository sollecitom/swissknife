package sollecitom.libs.swissknife.core.domain.security

/** A password value that masks its content in [toString]. Must be non-blank and trimmed. */
@JvmInline
value class Password(val value: String) : Comparable<Password> {

    init {
        require(value.isNotBlank())
        require(value.trim() == value)
    }

    override fun compareTo(other: Password) = value.compareTo(other.value)

    override fun toString() = "Password(value='*****')"
}