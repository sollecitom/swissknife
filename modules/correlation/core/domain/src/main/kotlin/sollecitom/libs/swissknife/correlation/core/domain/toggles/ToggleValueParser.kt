package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.core.domain.identity.Id

fun ToggleValue.Companion.withIdAndRawValue(id: Id, rawValue: String): ToggleValue<*> = when {
    rawValue.toLongOrNull() != null -> IntegerToggleValue(id, rawValue.toLong())
    rawValue.toDoubleOrNull() != null -> DecimalToggleValue(id, rawValue.toDouble())
    rawValue.toBooleanStrictOrNull() != null -> BooleanToggleValue(id, rawValue.toBooleanStrict())
    else -> EnumToggleValue(id, rawValue)
}