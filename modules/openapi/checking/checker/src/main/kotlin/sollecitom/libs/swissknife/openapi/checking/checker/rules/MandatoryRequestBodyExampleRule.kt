package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.openapi.checking.checker.model.OperationWithContext
import sollecitom.libs.swissknife.openapi.checking.checker.model.allOperations
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod
import io.swagger.v3.oas.models.media.MediaType

class MandatoryRequestBodyExampleRule(private val methods: Set<HttpMethod>, private val mediaTypesThatShouldHaveAnExample: Set<String>) : ComplianceRule<OpenAPI> {

    init {
        require(methods.isNotEmpty()) { "Must specify at least one method to check" }
        require(mediaTypesThatShouldHaveAnExample.isNotEmpty()) { "Must specify at least one media type name to check" }
    }

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violations = target.allOperations().asSequence().filter { it.operation.method in methods }.mapNotNull { operation -> check(operation) }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    private fun check(operation: OperationWithContext): Violation? {

        val body = operation.requestBody ?: return null
        val content = body.content?.takeUnless { it.isEmpty() } ?: return null
        val withoutMandatoryExample = mediaTypesThatShouldHaveAnExample.filter { mediaTypeName -> !content[mediaTypeName].isCompliant() }.toSet()
        if (withoutMandatoryExample.isNotEmpty()) return operation.violation(withoutMandatoryExample)
        return null
    }

    private fun MediaType?.isCompliant(): Boolean = this == null || hasAnExample()
    private fun MediaType.hasAnExample(): Boolean = (examples?.isNotEmpty() ?: false) || example != null

    private fun OperationWithContext.violation(mediaTypesWithoutMandatoryExample: Set<String>) = Violation(this, mediaTypesThatShouldHaveAnExample, mediaTypesWithoutMandatoryExample)

    class Violation(val operation: OperationWithContext, val mediaTypesThatShouldHaveAnExample: Set<String>, val mediaTypesWithoutMandatoryExample: Set<String>) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Operation ${operation.operation.method} on path ${operation.pathName} should have a request body example for media types ${mediaTypesThatShouldHaveAnExample.joinToString(prefix = "[", postfix = "]")}, but media types ${mediaTypesWithoutMandatoryExample.joinToString(prefix = "[", postfix = "]")} do not specify an example"
    }
}