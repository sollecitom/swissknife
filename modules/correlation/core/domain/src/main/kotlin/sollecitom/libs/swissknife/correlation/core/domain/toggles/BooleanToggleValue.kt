package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.core.domain.identity.Id

data class BooleanToggleValue(override val id: Id, override val value: Boolean) : ToggleValue<Boolean> {

    companion object
}