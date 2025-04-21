package sollecitom.libs.swissknife.avro.schema.repository.domain

import org.apache.avro.Schema

interface AvroSchemaLocator {

    fun resolve(referencedTypes: Map<String, Schema>): Schema
}

fun AvroSchemaLocator.resolve(referencedTypes: Set<Schema>) = resolve(referencedTypes.associateBy(Schema::getFullName))

fun AvroSchemaLocator.resolve(vararg referencedTypes: Schema) = resolve(referencedTypes.toSet())