package sollecitom.libs.swissknife.correlation.core.domain.toggles.standard.template

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.toggles.EnumToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggle
import sollecitom.libs.swissknife.correlation.core.domain.toggles.ToggleValue
import sollecitom.libs.swissknife.correlation.core.domain.toggles.Toggles

internal class EnumToggle<VALUE : Enum<VALUE>>(override val id: Id, private val deserializeValue: (String) -> VALUE) : Toggle<VALUE, String> {

    override operator fun invoke(toggles: Toggles) = toggles[id]?.let(ToggleValue<*>::value)?.let { it as String }?.let(deserializeValue)

    override fun invoke(value: VALUE) = EnumToggleValue(id = id, value = value.name)
}