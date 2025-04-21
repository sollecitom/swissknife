package sollecitom.libs.swissknife.correlation.core.domain.toggles

import sollecitom.libs.swissknife.core.domain.traits.Identifiable

interface Toggle<VALUE : Any, out SERIALIZED_VALUE : Any> : Identifiable, ToggleValueSerializer<VALUE, SERIALIZED_VALUE>, ToggleValueExtractor<VALUE> {

    companion object
}