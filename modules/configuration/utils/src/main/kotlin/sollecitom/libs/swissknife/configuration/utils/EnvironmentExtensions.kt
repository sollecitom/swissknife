package sollecitom.libs.swissknife.configuration.utils

import sollecitom.libs.swissknife.core.domain.identity.InstanceInfo
import sollecitom.libs.swissknife.kotlin.extensions.collections.toPairsArray
import sollecitom.libs.swissknife.resource.utils.ResourceLoader
import org.http4k.config.Environment
import org.http4k.config.EnvironmentKey
import org.http4k.config.MapEnvironment
import org.http4k.config.fromYaml
import org.http4k.format.JacksonYaml
import org.http4k.lens.BiDiLens
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader

/** Creates an [Environment] from lens-value pairs, using each lens's meta name as the key. */
fun Environment.Companion.from(vararg entries: Pair<BiDiLens<Environment, *>, String>) = from(*entries.map { it.first.meta.name to it.second }.toTypedArray())

fun Environment.Companion.from(entries: Map<BiDiLens<Environment, *>, String>) = from(*entries.toPairsArray())

/** Loads an [Environment] from a YAML file on the classpath. */
fun Environment.Companion.fromYamlResource(resourceName: String): Environment {

    val stream = ResourceLoader.openAsStream(resourceName)
    return Environment.fromYaml(stream)
}

/** Parses a YAML input stream into a flat [Environment], flattening nested keys with dot notation. */
fun Environment.Companion.fromYaml(input: InputStream): Environment {

    val map = JacksonYaml.asA<Map<String, Any>>(InputStreamReader(input).use { it.readText() })

    fun Map<*, *>.flatten(): List<Pair<String, String>> {
        fun convert(key: Any?, value: Any?): List<Pair<String, String>> {
            val keyString = (key ?: "").toString()

            return when (value) {
                is List<*> -> listOf(keyString to value.flatMap { convert(null, it) }.joinToString(",") { it.second })
                is Map<*, *> -> value.flatten().map { "$keyString.${it.first}" to it.second }
                else -> listOf((keyString to (value ?: "").toString()))
            }
        }

        return entries.fold(listOf()) { acc, (key, value) -> acc + convert(key, value) }
    }

    return MapEnvironment.from(map.flatten().toMap().toProperties())
}

fun Environment.instanceInfo(): InstanceInfo = InstanceInfo(EnvironmentKey.instanceNodeName(this), EnvironmentKey.instanceGroupName(this))

/** Loads and merges multiple YAML files into a single [Environment], with earlier files taking precedence. */
fun Environment.Companion.fromFiles(files: List<File>): Environment = files.map { Environment.fromYaml(it) }.foldRight(EMPTY) { item, accumulator -> item overrides accumulator }

/** Returns a human-readable representation of all environment keys and values. */
fun Environment.formatted(): String = "Environment: {\n\t${keys().joinToString(separator = "\n\t", postfix = "\n}") { key -> "$key: ${get(key)}" }}"

/**
 * Returns the properties under [root], keyed by the matching name in [knownNames].
 * A key matches a name however it's spelled (`root.operationTimeoutMs`, `ROOT_OPERATION_TIMEOUT_MS`, …), and any key under [root] that matches no known name is an error.
 */
fun Environment.configurationPropertiesUnderRoot(root: String, knownNames: Set<String>): Map<String, String> {

    val namesByNormalisedForm = knownNames.groupBy { it.normalisedPropertyName() }
    namesByNormalisedForm.filterValues { it.size > 1 }.values.firstOrNull()?.let { colliding -> throw IllegalArgumentException("Known property names $colliding can't be told apart once normalised") }
    val prefix = "${root.lowercase().replace('.', '-').replace('_', '-')}-"
    val propertiesByNormalisedName = keys().filter { it.startsWith(prefix) }.associate { key -> key.removePrefix(prefix).normalisedPropertyName() to get(key)!! }
    val unknown = propertiesByNormalisedName.keys - namesByNormalisedForm.keys
    require(unknown.isEmpty()) { "Unknown configuration properties under '$root': $unknown. Known properties are $knownNames" }
    return propertiesByNormalisedName.mapKeys { (normalisedName, _) -> namesByNormalisedForm.getValue(normalisedName).single() }
}

private fun String.normalisedPropertyName() = lowercase().filter(Char::isLetterOrDigit)