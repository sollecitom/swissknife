package sollecitom.libs.swissknife.lens.correlation.extensions.toggles

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.fromString
import sollecitom.libs.swissknife.correlation.core.domain.toggles.ToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.withIdAndRawValue
import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.BiDiMapping
import org.http4k.lens.StringBiDiMappings
import org.http4k.lens.map

fun <IN : Any> BiDiLensSpec<IN, String>.toggleValue() = map(StringBiDiMappings.toggleValue())
fun StringBiDiMappings.toggleValue() = BiDiMapping(ToggleValue.Companion::deserialize, ToggleValue<*>::serialize)

private fun ToggleValue.Companion.deserialize(raw: String): ToggleValue<*> {

    val parts = raw.split("=")
    require(parts.size == 2) { "Toggle values are serialized as '{id}={value}'. Raw value '$raw' is illegal." }
    val (rawId, rawValue) = parts
    return ToggleValue.withIdAndRawValue(id = Id.fromString(rawId), rawValue = rawValue)
}

private fun ToggleValue<*>.serialize(): String = "${id.stringValue}=${value}"