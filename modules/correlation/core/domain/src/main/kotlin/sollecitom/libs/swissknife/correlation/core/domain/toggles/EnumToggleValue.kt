package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.core.domain.identity.Id

data class EnumToggleValue(override val id: Id, override val value: String) : ToggleValue<String> {

    companion object
}