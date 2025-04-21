package sollecitom.libs.swissknife.core.domain.security

@JvmInline
value class Password(val value: String) : Comparable<Password> {

    init {
        require(value.isNotBlank())
        require(value.trim() == value)
    }

    override fun compareTo(other: Password) = value.compareTo(other.value)

    override fun toString() = "Password(value='*****')"
}