package sollecitom.libs.swissknife.avro.schema.repository.domain

import sollecitom.libs.swissknife.core.domain.text.Name

interface AvroSchemaRepository {

    fun getByFullyQualifiedNameOrNull(name: AvroSchemaName): AvroSchemaLocator?

    fun findAllInNamespace(namespace: Name): Sequence<AvroSchemaLocator>
}

fun AvroSchemaRepository.getByFullyQualifiedName(name: AvroSchemaName) = getByFullyQualifiedNameOrNull(name) ?: error("No schema found for name $name")