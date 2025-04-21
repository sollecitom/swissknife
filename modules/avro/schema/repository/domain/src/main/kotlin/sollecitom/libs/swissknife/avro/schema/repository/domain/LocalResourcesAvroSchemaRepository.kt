package sollecitom.libs.swissknife.avro.schema.repository.domain

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.resource.utils.ResourceLoader
import io.github.classgraph.ClassGraph
import java.io.File

class LocalResourcesAvroSchemaRepository(private val rootPackage: String, private val extension: String = defaultExtension) : AvroSchemaRepository {

    private val classGraph: ClassGraph by lazy(::ClassGraph)

    override fun getByFullyQualifiedNameOrNull(name: AvroSchemaName): AvroSchemaLocator? = runCatching {
        name.path().also {
            ensureResourceAtPathExists(it)
        }.let(::ResourceAvroSchemaLocator)
    }.getOrNull()

    private fun ensureResourceAtPathExists(path: String) {
        ResourceLoader.readAsText(path)
    }

    override fun findAllInNamespace(namespace: Name): Sequence<AvroSchemaLocator> {

        val resources = classGraph.acceptPaths(namespace.directory()).scan().use { it.getResourcesWithExtension(extension) }.asSequence()
        return resources.map { ResourceAvroSchemaLocator(it.path) }
    }

    private fun AvroSchemaName.path(): String = "${namespace.directory()}/${name.value}.$extension"
    private fun Name.directory(): String = "$rootPackage/${asPath()}"
    private fun Name.asPath(): String = value.split(".").joinToString(separator = File.separator)

    companion object {
        const val defaultExtension = "avsc"
    }
}

