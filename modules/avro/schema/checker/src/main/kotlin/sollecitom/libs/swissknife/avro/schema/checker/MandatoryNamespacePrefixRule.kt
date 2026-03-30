package sollecitom.libs.swissknife.avro.schema.checker

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import org.apache.avro.Schema

/** Compliance rule that ensures the Avro schema namespace starts with the given [prefix]. */
data class MandatoryNamespacePrefixRule(private val prefix: String) : ComplianceRule<Schema> {

    override fun invoke(target: Schema): ComplianceRule.Result<Schema> {

        val violation = check(target.namespace)
        return ComplianceRule.Result.withViolationOrNull(violation)
    }

    private fun check(namespace: String): Violation? {

        if (!namespace.startsWith(prefix)) return violation(namespace)
        return null
    }

    private fun violation(namespace: String) = Violation(namespace = namespace, prefix = prefix)

    data class Violation(val namespace: String, val prefix: String) : ComplianceRule.Result.Violation<Schema> {

        override val message = "Namespace name '$namespace' should start with prefix '$prefix' but doesn't"
    }
}