package sollecitom.libs.swissknife.core.domain.email

import sollecitom.libs.swissknife.kotlin.extensions.text.withoutWhitespace

@JvmInline
value class EmailAddress(val value: String) : Comparable<EmailAddress> {

    init {
        // TODO improve email validation
        require(value.isNotBlank()) { "email address cannot be blank" }
        require(value.withoutWhitespace() == value) { "email address cannot contain whitespace" }
        require(PREFIX_FROM_DOMAIN_SEPARATOR in value) { "email address must contain '$PREFIX_FROM_DOMAIN_SEPARATOR'" }
        val lastDomainSeparator = value.filter { it == DOMAIN_SEPARATOR }.find { char -> value.lastIndexOf(char) > value.lastIndexOf(PREFIX_FROM_DOMAIN_SEPARATOR) }
        require(lastDomainSeparator != null) { "email address must contain a '$DOMAIN_SEPARATOR' character after the '$PREFIX_FROM_DOMAIN_SEPARATOR' character" }
    }

    override fun compareTo(other: EmailAddress) = value.compareTo(other.value)

    companion object {

        const val PREFIX_FROM_DOMAIN_SEPARATOR = '@'
        const val DOMAIN_SEPARATOR = '.'
    }
}