package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.correlation.core.domain.toggles.standard.invocation.visibility.InvocationVisibility

/** The toggles defined by swissknife, whose values are validated when parsed from untrusted input. */
val Toggles.Companion.standard: Set<Toggle<*, *>> get() = setOf(Toggles.InvocationVisibility)

/** Fails if [value] belongs to one of [toggles] but is not a legal value for it. */
fun ToggleValue<*>.requireAcceptedBy(toggles: Set<Toggle<*, *>> = Toggles.standard): ToggleValue<*> = apply {
    toggles.firstOrNull { it.id == id }?.let { toggle -> require(toggle.accepts(this)) { "Value '$value' is not legal for toggle '${id.stringValue}'" } }
}
