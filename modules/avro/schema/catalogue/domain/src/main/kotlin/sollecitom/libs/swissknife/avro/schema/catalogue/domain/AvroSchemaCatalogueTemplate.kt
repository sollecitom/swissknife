package sollecitom.libs.swissknife.avro.schema.catalogue.domain

import sollecitom.libs.swissknife.avro.schema.repository.domain.*
import sollecitom.libs.swissknife.core.domain.text.Name
import org.apache.avro.Schema

/**
 * Base class for defining an Avro schema catalogue. Subclasses declare schemas by calling [getSchema].
 * Schemas are loaded from classpath resources under the given [rootPackage] directory.
 */
abstract class AvroSchemaCatalogueTemplate(namespace: String, rootPackage: String = defaultRootPackage) : AvroSchemaContainer {

    final override val namespace: Name = namespace.let(::Name)

    private val repository: AvroSchemaRepository = rootPackage.takeIf { it == defaultRootPackage }?.let { rootPackageRepository } ?: LocalResourcesAvroSchemaRepository(rootPackage)

    protected fun getSchema(name: String, dependencies: Set<Schema> = emptySet()): Schema = repository.getByFullyQualifiedName(AvroSchemaName(namespace, name.let(::Name))).resolve(dependencies)

    private companion object {

        private const val defaultRootPackage = "avro/schemas"
        private val rootPackageRepository by lazy { LocalResourcesAvroSchemaRepository(defaultRootPackage) }
    }
}