package sollecitom.libs.swissknife.core.domain.email

import sollecitom.libs.swissknife.kotlin.extensions.text.withoutWhitespace

/** A validated email address. Enforces basic format rules: no whitespace, exactly one '@' with a non-empty local part, and a dotted domain without empty labels. */
@JvmInline
value class EmailAddress(val value: String) : Comparable<EmailAddress> {

    init {
        require(value.isNotBlank()) { "email address cannot be blank" }
        require(value.withoutWhitespace() == value) { "email address cannot contain whitespace" }
        require(value.count { it == PREFIX_FROM_DOMAIN_SEPARATOR } == 1) { "email address must contain exactly one '$PREFIX_FROM_DOMAIN_SEPARATOR'" }
        val localPart = value.substringBefore(PREFIX_FROM_DOMAIN_SEPARATOR)
        val domain = value.substringAfter(PREFIX_FROM_DOMAIN_SEPARATOR)
        require(localPart.isNotEmpty()) { "email address must have a non-empty part before the '$PREFIX_FROM_DOMAIN_SEPARATOR'" }
        require(DOMAIN_SEPARATOR in domain) { "email address must contain a '$DOMAIN_SEPARATOR' character after the '$PREFIX_FROM_DOMAIN_SEPARATOR' character" }
        require(domain.split(DOMAIN_SEPARATOR).none(String::isEmpty)) { "email address domain cannot have empty labels" }
    }

    override fun compareTo(other: EmailAddress) = value.compareTo(other.value)

    companion object {

        const val PREFIX_FROM_DOMAIN_SEPARATOR = '@'
        const val DOMAIN_SEPARATOR = '.'
    }
}