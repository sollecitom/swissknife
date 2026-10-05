package sollecitom.libs.swissknife.avro.schema.checker

import org.apache.avro.JsonProperties
import org.apache.avro.Schema
import sollecitom.libs.swissknife.compliance.checker.domain.ComplianceRule

/** Compliance rule that ensures every union containing `null`, anywhere in the schema, lists `null` first, and that every field of such a union type declares `"default": null`. */
data object NullFirstNullableUnionsRule : ComplianceRule<Schema> {

    override fun invoke(target: Schema): ComplianceRule.Result<Schema> {

        val nodes = target.nodes().toList()
        val unionViolations = nodes.filter { it.schema.isNullableUnion && it.schema.types.first().type != Schema.Type.NULL }.map { Violation.NullNotFirst(path = it.path) }
        val fieldViolations = nodes.filter { it.schema.type == Schema.Type.RECORD }.flatMap { record -> record.schema.fields.filter { it.schema().isNullableUnion && !it.defaultsToNull }.map { Violation.MissingNullDefault(path = "${record.path}.${it.name()}") } }
        return ComplianceRule.Result.withViolations((unionViolations + fieldViolations).toSet())
    }

    private val Schema.isNullableUnion: Boolean get() = type == Schema.Type.UNION && types.any { it.type == Schema.Type.NULL }

    private val Schema.Field.defaultsToNull: Boolean get() = hasDefaultValue() && defaultVal() == JsonProperties.NULL_VALUE

    sealed interface Violation : ComplianceRule.Result.Violation<Schema> {

        data class NullNotFirst(val path: String) : Violation {

            override val message = "The union at '$path' contains null, so it should list null first, but doesn't"
        }

        data class MissingNullDefault(val path: String) : Violation {

            override val message = "Field '$path' is nullable, so it should declare \"default\": null, but doesn't"
        }
    }
}
