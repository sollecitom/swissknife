package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.core.domain.traits.Identifiable

/** A concrete toggle value with an identifying [Id], carrying a typed [value]. */
sealed interface ToggleValue<out VALUE : Any> : Identifiable {

    val value: VALUE

    companion object
}