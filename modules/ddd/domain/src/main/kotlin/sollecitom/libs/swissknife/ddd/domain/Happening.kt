package sollecitom.libs.swissknife.ddd.domain

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.core.domain.versioning.Versioned

sealed interface Happening : Versioned<IntVersion> {

    val type: Type
    override val version: IntVersion get() = type.version

    data class Type(val name: Name, override val version: IntVersion) : Versioned<IntVersion> {

        val stringValue = "${name.value}--v${version.value}"

        companion object {

            fun parse(stringValue: String): Type {

                val parts = stringValue.split("--v")
                check(parts.size == 2) { "Problematic raw value: $stringValue" }
                val name = parts[0].let(::Name)
                val version = parts[1].toInt().let(::IntVersion)
                return Type(name, version)
            }
        }
    }

    companion object
}