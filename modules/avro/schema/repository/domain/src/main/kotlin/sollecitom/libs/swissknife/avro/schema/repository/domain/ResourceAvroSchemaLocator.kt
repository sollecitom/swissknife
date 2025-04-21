package sollecitom.libs.swissknife.avro.schema.repository.domain

import sollecitom.libs.swissknife.avro.serialization.utils.avroSchemaAt
import org.apache.avro.Schema

internal class ResourceAvroSchemaLocator(private val path: String) : AvroSchemaLocator {

    override fun resolve(referencedTypes: Map<String, Schema>) = avroSchemaAt(path, referencedTypes)
}