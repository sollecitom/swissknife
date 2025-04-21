package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.core.domain.identity.Id

data class DecimalToggleValue(override val id: Id, override val value: Double) : ToggleValue<Double> {

    companion object
}