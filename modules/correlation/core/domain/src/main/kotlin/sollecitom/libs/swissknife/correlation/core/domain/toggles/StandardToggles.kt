package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.correlation.core.domain.toggles.standard.invocation.visibility.InvocationVisibility

val Toggles.Companion.standard: Set<Toggle<*, *>> get() = setOf(Toggles.InvocationVisibility)

fun ToggleValue<*>.requireAcceptedBy(toggles: Set<Toggle<*, *>> = Toggles.standard): ToggleValue<*> = apply {
    toggles.firstOrNull { it.id == id }?.let { toggle -> require(toggle.accepts(this)) { "Value '$value' is not legal for toggle '${id.stringValue}'" } }
}
