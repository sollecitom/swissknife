package sollecitom.libs.swissknife.json.utils

import com.github.erosb.jsonsKema.*
import org.json.JSONArray
import org.json.JSONObject
import com.github.erosb.jsonsKema.ValidationFailure as SkemaValidationFailure

data class JsonSchema(internal val value: Schema, private val source: JSONObject? = null) {

    val description: String get() = source?.toString() ?: value.toString()

    val location: String get() = value.location.getLocation()

    val isAPureUnionType: Boolean get() = properties.isEmpty() && value.subschemas().filterIsInstance<OneOfSchema>().singleOrNull() != null

    val requiredPropertyNames: Set<String> by lazy {
        value.subschemas().filterIsInstance<RequiredSchema>().singleOrNull()?.requiredProperties?.toSet() ?: emptySet()
    }

    val propertyNames: Set<String> by lazy {
        properties.map { it.name }.toSet()
    }

    val properties: Set<Property> by lazy {
        val schema = value as CompositeSchema? ?: return@lazy emptySet()
        schema.propertySchemas.map { (fieldName, fieldSchema) -> Property(fieldName, JsonSchema(fieldSchema)) }.toSet()
    }

    val allowsAdditionalProperties: Boolean? by lazy {
        value.subschemas().filterIsInstance<AdditionalPropertiesSchema>().singleOrNull()?.subschema?.let { if (it is TrueSchema) true else if (it is FalseSchema) false else null }
    }

    fun validate(json: JSONObject, parentPath: List<String> = emptyList()): ValidationFailure? = value.validate(json)?.adapted(parentPath)

    fun validate(json: JSONArray, parentPath: List<String> = emptyList()): ValidationFailure? = value.validate(json)?.adapted(parentPath)

    private fun Schema.validate(json: IJsonValue) = Validator.forSchema(this).validate(json)

    private fun Schema.validate(json: JSONObject) = validate(JsonParser(json.toString()).parse())

    private fun Schema.validate(json: JSONArray) = validate(JsonParser(json.toString()).parse())

    private fun SkemaValidationFailure.adapted(parentPath: List<String>) = ValidationFailure(this, parentPath)

    data class ValidationFailure(private val failure: SkemaValidationFailure, private val parentPath: List<String>) {

        val message: String get() = failure.toString()
        val location: String get() = failure.instance.location.getLocation()
        private val path: List<String> get() = failure.schema.location.pointer.segments
        private val fullPath = parentPath + "schema" + path
        val fullPathAsString = fullPath.joinToString(separator = ".")
    }

    data class Property(val name: String, val schema: JsonSchema)
}