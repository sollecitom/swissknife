package sollecitom.libs.swissknife.json.utils.checker.rules

import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule
import sollecitom.libs.swissknife.json.utils.JsonSchema
import com.github.erosb.jsonsKema.ConstSchema

data object DisallowConstKeywordRule : ComplianceRule<JsonSchema> {

    override fun invoke(target: JsonSchema): ComplianceRule.Result<JsonSchema> {

        val violations = target.objectSchemas.flatMap { (path, schema) -> schema.properties.filter { it.schema.value.subschemas().any { subschema -> subschema is ConstSchema } }.map { Violation(path + it.name) } }.toSet()
        return ComplianceRule.Result.withViolations(violations)
    }

    data class Violation(val path: List<String>) : ComplianceRule.Result.Violation<JsonSchema> {

        override val message = "JSON schema shouldn't restrict the value of a property with the 'const' keyword, but does (use 'enum' instead) at ${path.joinToString(".")}"
    }
}