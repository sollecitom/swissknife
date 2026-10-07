package sollecitom.libs.swissknife.lens.correlation.extensions.toggles

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.fromString
import sollecitom.libs.swissknife.correlation.core.domain.toggles.ToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.requireAcceptedBy
import sollecitom.libs.swissknife.correlation.core.domain.toggles.withIdAndRawValue
import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.BiDiMapping
import org.http4k.lens.StringBiDiMappings
import org.http4k.lens.map

fun <IN : Any> BiDiLensSpec<IN, String>.toggleValue() = map(StringBiDiMappings.toggleValue())
fun StringBiDiMappings.toggleValue() = BiDiMapping(ToggleValue.Companion::deserialize, ToggleValue<*>::serialize)

private fun ToggleValue.Companion.deserialize(raw: String): ToggleValue<*> {

    require(SEPARATOR in raw) { "Toggle values are serialized as '{id}$SEPARATOR{value}'. Raw value '$raw' is illegal." }
    val rawId = raw.substringBefore(SEPARATOR)
    val rawValue = raw.substringAfter(SEPARATOR)
    return ToggleValue.withIdAndRawValue(id = Id.fromString(rawId), rawValue = rawValue).requireAcceptedBy()
}

private fun ToggleValue<*>.serialize(): String = "${id.stringValue}$SEPARATOR${value}"

private const val SEPARATOR = '='