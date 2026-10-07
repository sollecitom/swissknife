package sollecitom.libs.swissknife.json.utils

import com.github.erosb.jsonsKema.*
import org.json.JSONArray
import org.json.JSONObject
import com.github.erosb.jsonsKema.ValidationFailure as SkemaValidationFailure

/** A parsed JSON Schema that supports validation and property introspection. */
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
        val schema = value as? CompositeSchema ?: return@lazy emptySet()
        schema.propertySchemas.map { (fieldName, fieldSchema) -> Property(fieldName, JsonSchema(fieldSchema)) }.toSet()
    }

    val declaresAdditionalProperties: Boolean by lazy { value.subschemas().any { it is AdditionalPropertiesSchema } }

    val allowsAdditionalProperties: Boolean? by lazy {
        value.subschemas().filterIsInstance<AdditionalPropertiesSchema>().singleOrNull()?.subschema?.let { if (it is TrueSchema) true else if (it is FalseSchema) false else null }
    }

    val objectSchemas: List<Pair<List<String>, JsonSchema>> by lazy { listOf(emptyList<String>() to this) + nestedObjectSchemas(emptyList()) }

    private fun nestedObjectSchemas(path: List<String>): List<Pair<List<String>, JsonSchema>> = buildList {
        properties.forEach { property ->
            val propertyPath = path + property.name
            if (property.schema.isObject) add(propertyPath to property.schema)
            addAll(property.schema.nestedObjectSchemas(propertyPath))
        }
        items?.let { items ->
            val itemsPath = path + ITEMS_PATH_SEGMENT
            if (items.isObject) add(itemsPath to items)
            addAll(items.nestedObjectSchemas(itemsPath))
        }
    }

    private val isObject: Boolean get() = properties.isNotEmpty() || value.subschemas().filterIsInstance<TypeSchema>().any { it.type.value == OBJECT_TYPE }

    private val items: JsonSchema? get() = value.subschemas().filterIsInstance<ItemsSchema>().singleOrNull()?.itemsSchema?.let(::JsonSchema)

    fun validate(json: JSONObject, parentPath: List<String> = emptyList()): ValidationFailure? = value.validate(json)?.adapted(parentPath)

    fun validate(json: JSONArray, parentPath: List<String> = emptyList()): ValidationFailure? = value.validate(json)?.adapted(parentPath)

    fun validate(json: String, parentPath: List<String> = emptyList()): ValidationFailure? = value.validate(json)?.adapted(parentPath)

    fun validate(json: Number, parentPath: List<String> = emptyList()): ValidationFailure? = value.validate(json)?.adapted(parentPath)

    fun validate(json: Boolean, parentPath: List<String> = emptyList()): ValidationFailure? = value.validate(json)?.adapted(parentPath)

    private fun Schema.validate(json: Any) = JSONObject.valueToString(json).let(::JsonParser).parse().let(Validator.forSchema(this)::validate)

    private fun SkemaValidationFailure.adapted(parentPath: List<String>) = ValidationFailure(this, parentPath)

    data class ValidationFailure(private val failure: SkemaValidationFailure, private val parentPath: List<String>) {

        val message: String get() = failure.toString()
        val location: String get() = failure.instance.location.getLocation()
        private val path: List<String> get() = failure.schema.location.pointer.segments
        private val fullPath = parentPath + "schema" + path
        val fullPathAsString = fullPath.joinToString(separator = ".")
    }

    data class Property(val name: String, val schema: JsonSchema)

    companion object {
        const val ITEMS_PATH_SEGMENT = "[]"
        private const val OBJECT_TYPE = "object"
    }
}