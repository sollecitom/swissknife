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

fun Environment.Companion.from(vararg entries: Pair<BiDiLens<Environment, *>, String>) = from(*entries.map { it.first.meta.name to it.second }.toTypedArray())

fun Environment.Companion.from(entries: Map<BiDiLens<Environment, *>, String>) = from(*entries.toPairsArray())

fun Environment.Companion.fromYamlResource(resourceName: String): Environment {

    val stream = ResourceLoader.openAsStream(resourceName)
    return Environment.fromYaml(stream)
}

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

fun Environment.Companion.fromFiles(files: List<File>): Environment = files.map { Environment.fromYaml(it) }.foldRight(EMPTY) { item, accumulator -> item overrides accumulator }

fun Environment.formatted(): String = "Environment: {\n\t${keys().joinToString(separator = "\n\t", postfix = "\n}") { key -> "$key: ${get(key)}" }}"

fun Environment.configurationPropertiesUnderRoot(root: String): Map<String, Any?> = keys().filter { key -> key.startsWith(root) }.map { it.removePrefix(root) }.associateWith(::get)