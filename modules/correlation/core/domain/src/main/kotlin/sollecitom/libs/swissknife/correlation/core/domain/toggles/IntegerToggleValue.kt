package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.core.domain.identity.Id

data class IntegerToggleValue(override val id: Id, override val value: Long) : ToggleValue<Long> {

    companion object
}