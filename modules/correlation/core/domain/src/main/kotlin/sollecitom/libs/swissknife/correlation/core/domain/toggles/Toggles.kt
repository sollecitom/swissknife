package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext

data class Toggles(val values: Set<ToggleValue<*>> = emptySet()) {

    private val byId = values.associateBy(ToggleValue<*>::id)

    operator fun get(toggleId: Id): ToggleValue<*>? = byId[toggleId]

    companion object
}

@Suppress("UNCHECKED_CAST")
operator fun <VALUE : Any> Toggles.get(toggle: ToggleValueExtractor<VALUE>): VALUE? {

    val rawValue = get(toggle.id)?.let(ToggleValue<*>::value)
    return rawValue?.let { it as VALUE }
}

fun <ACCESS : Access, VALUE : Any> InvocationContext<ACCESS>.withToggle(toggle: Toggle<VALUE, VALUE>, value: VALUE) = copy(toggles = toggles.withToggle(toggle, value))

fun <ACCESS : Access, VALUE : Enum<VALUE>> InvocationContext<ACCESS>.withToggle(toggle: Toggle<VALUE, *>, value: VALUE) = copy(toggles = toggles.withToggle(toggle, value))

fun <VALUE : Enum<VALUE>> Toggles.withToggle(toggle: Toggle<VALUE, *>, value: VALUE): Toggles = copy(values = values + toggle(value))

fun <VALUE : Any> Toggles.withToggle(toggle: Toggle<VALUE, VALUE>, value: VALUE): Toggles = copy(values = values + toggle(value))