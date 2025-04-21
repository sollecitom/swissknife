package sollecitom.libs.swissknife.lens.core.extensions.base

import org.http4k.lens.BiDiLensSpec
import org.http4k.lens.BiDiMapping
import org.http4k.lens.StringBiDiMappings
import org.http4k.lens.map
import java.net.URI

fun <IN : Any> BiDiLensSpec<IN, String>.javaURI() = map(StringBiDiMappings.javaURI())

fun StringBiDiMappings.javaURI() = BiDiMapping(URI::create, URI::toString)