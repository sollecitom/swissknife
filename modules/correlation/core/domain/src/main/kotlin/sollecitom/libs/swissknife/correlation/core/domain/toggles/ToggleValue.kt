package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.core.domain.traits.Identifiable

sealed interface ToggleValue<out VALUE : Any> : Identifiable {

    val value: VALUE

    companion object
}