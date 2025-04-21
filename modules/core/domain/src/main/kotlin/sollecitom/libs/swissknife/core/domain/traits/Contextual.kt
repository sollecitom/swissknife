package sollecitom.libs.swissknife.core.domain.traits

interface Contextual<out CONTEXT : Any> {

    val context: CONTEXT
}