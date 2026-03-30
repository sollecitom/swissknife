package sollecitom.libs.swissknife.core.domain.traits

/** Marks a type that carries contextual metadata. */
interface Contextual<out CONTEXT : Any> {

    val context: CONTEXT
}