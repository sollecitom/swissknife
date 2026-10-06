package sollecitom.libs.swissknife.ddd.domain

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.core.domain.versioning.Versioned

/** Base type for versioned domain occurrences (events, commands, queries). Each has a [Type] with a name and version. */
sealed interface Happening : Versioned<IntVersion> {

    val type: Type
    override val version: IntVersion get() = type.version

    /** A versioned type identifier for a [Happening], serialized as "name--vN". */
    data class Type(val name: Name, override val version: IntVersion) : Versioned<IntVersion> {

        val stringValue = "${name.value}$VERSION_SEPARATOR${version.value}"

        companion object {

            private const val VERSION_SEPARATOR = "--v"

            fun parse(stringValue: String): Type {

                check(VERSION_SEPARATOR in stringValue) { "Problematic raw value: $stringValue" }
                val name = stringValue.substringBeforeLast(VERSION_SEPARATOR).let(::Name)
                val version = stringValue.substringAfterLast(VERSION_SEPARATOR).toInt().let(::IntVersion)
                return Type(name, version)
            }
        }
    }

    companion object
}