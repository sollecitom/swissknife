package sollecitom.libs.swissknife.web.api.utils.negotiation

import org.http4k.format.auto
import org.http4k.lens.BiDiBodyLens
import org.http4k.lens.ContentNegotiation

/** Creates automatic content negotiation from a list of body lenses (convenience overload accepting a [List] instead of varargs). */
fun <T> ContentNegotiation.Companion.auto(lenses: List<BiDiBodyLens<T>>) = auto(lenses.first(), *lenses.drop(1).toTypedArray())