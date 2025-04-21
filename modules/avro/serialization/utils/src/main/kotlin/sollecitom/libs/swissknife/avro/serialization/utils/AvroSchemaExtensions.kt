package sollecitom.libs.swissknife.avro.serialization.utils

import org.apache.avro.AvroRuntimeException
import org.apache.avro.Schema

val Schema.referencedSchemas: Set<Schema>
    get() = fields().mapNotNull { field ->
        when {
            field.schema().isUnion -> field.schema().types.flatMap(Schema::referencedSchemas)
            else -> field.schema().takeIf { field.referencesAnotherType }?.let(::setOf)
        }
    }.flatten().toSet()

private fun Schema.fields(): List<Schema.Field> = try {
    fields
} catch (e: AvroRuntimeException) {
    emptyList()
}

private val Schema.Field.referencesAnotherType: Boolean get() = "." in schema().fullName