package sollecitom.libs.swissknife.avro.schema.checker

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import org.apache.avro.Schema

data class WhitelistedAlphabetNamespaceNameRule(val alphabet: Set<Char>) : ComplianceRule<Schema> {

    override fun invoke(target: Schema): ComplianceRule.Result<Schema> {

        val violation = check(target.namespace)
        return ComplianceRule.Result.withViolationOrNull(violation)
    }

    private fun check(namespace: String): Violation? {

        if (namespace.any { character -> character !in alphabet }) return violation(namespace)
        return null
    }

    private fun violation(namespace: String) = Violation(namespace = namespace, alphabet = alphabet)

    data class Violation(val namespace: String, val alphabet: Set<Char>) : ComplianceRule.Result.Violation<Schema> {

        override val message = "Namespace name '$namespace' should only contain characters in $alphabet but doesn't"
    }

    companion object
}