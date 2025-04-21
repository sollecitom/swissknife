package sollecitom.libs.swissknife.avro.schema.catalogue.domain

import sollecitom.libs.swissknife.avro.schema.repository.domain.*
import sollecitom.libs.swissknife.core.domain.text.Name
import org.apache.avro.Schema

abstract class AvroSchemaCatalogueTemplate(namespace: String, rootPackage: String = defaultRootPackage) : AvroSchemaContainer {

    final override val namespace: Name = namespace.let(::Name)

    private val repository: AvroSchemaRepository = rootPackage.takeIf { it == defaultRootPackage }?.let { rootPackageRepository } ?: LocalResourcesAvroSchemaRepository(rootPackage)

    protected fun getSchema(name: String, dependencies: Set<Schema> = emptySet()): Schema = repository.getByFullyQualifiedName(AvroSchemaName(namespace, name.let(::Name))).resolve(dependencies)

    private companion object {

        private const val defaultRootPackage = "avro/schemas"
        private val rootPackageRepository by lazy { LocalResourcesAvroSchemaRepository(defaultRootPackage) }
    }
}