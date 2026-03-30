package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.core.domain.traits.Identifiable

/** A feature toggle that can extract and serialize values. Identified by a unique [Id]. */
interface Toggle<VALUE : Any, out SERIALIZED_VALUE : Any> : Identifiable, ToggleValueSerializer<VALUE, SERIALIZED_VALUE>, ToggleValueExtractor<VALUE> {

    companion object
}