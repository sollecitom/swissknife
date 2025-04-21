package sollecitom.libs.swissknife.openapi.checking.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.openapi.checking.checker.model.OpenApiField
import sollecitom.libs.swissknife.openapi.checking.checker.rules.utils.trimmed
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info as OpenApiInfo

class MandatoryInfoFieldsRule(private val requiredFields: Set<OpenApiField<OpenApiInfo, Any?>>) : ComplianceRule<OpenAPI> {

    override fun invoke(target: OpenAPI): ComplianceRule.Result<OpenAPI> {

        val violation = check(target.info)
        return ComplianceRule.Result.withViolationOrNull(violation)
    }

    private fun check(info: OpenApiInfo?): Violation? {

        val missingRequiredFields = requiredFields.filter { field -> info?.let { field.getter(it)?.trimmed() == null } ?: true }.toSet()
        if (missingRequiredFields.isNotEmpty()) return Violation(requiredFields, missingRequiredFields)
        return null
    }

    data class Violation(val requiredFields: Set<OpenApiField<OpenApiInfo, Any?>>, val missingRequiredFields: Set<OpenApiField<OpenApiInfo, Any?>>) : ComplianceRule.Result.Violation<OpenAPI> {

        override val message = "Info section should specify the following mandatory fields ${requiredFields.map(OpenApiField<OpenApiInfo, *>::name)}, but fields ${missingRequiredFields.map(OpenApiField<OpenApiInfo, *>::name)} were missing"
    }
}