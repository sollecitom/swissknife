package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.traits.Identifiable
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext

interface ToggleValueExtractor<out VALUE : Any> : Identifiable {

    operator fun invoke(toggles: Toggles): VALUE? = toggles[this]

    interface WithDefaultValue<VALUE : Any> : ToggleValueExtractor<VALUE> {

        val defaultValue: VALUE

        override operator fun invoke(toggles: Toggles): VALUE = super.invoke(toggles) ?: defaultValue
    }
}

private data class WithDefaultValueToggleValueExtractorAdapter<VALUE : Any>(override val defaultValue: VALUE, private val delegate: ToggleValueExtractor<VALUE>) : ToggleValueExtractor.WithDefaultValue<VALUE> {

    override val id: Id get() = delegate.id

    override fun invoke(toggles: Toggles) = delegate.invoke(toggles) ?: defaultValue
}

fun <VALUE : Any> ToggleValueExtractor<VALUE>.withDefaultValue(defaultValue: VALUE): ToggleValueExtractor.WithDefaultValue<VALUE> = WithDefaultValueToggleValueExtractorAdapter(defaultValue, this)

operator fun <VALUE : Any> ToggleValueExtractor<VALUE>.invoke(context: InvocationContext<*>): VALUE? = invoke(context.toggles)

operator fun <VALUE : Any> ToggleValueExtractor.WithDefaultValue<VALUE>.invoke(context: InvocationContext<*>): VALUE = invoke(context.toggles)