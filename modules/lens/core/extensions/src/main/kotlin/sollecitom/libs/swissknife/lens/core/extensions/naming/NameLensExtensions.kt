package sollecitom.libs.swissknife.lens.core.extensions.naming

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.lens.core.extensions.map
import org.http4k.lens.*

fun Path.name() = map(StringBiDiMappings.name())

fun <IN : Any> BiDiLensSpec<IN, String>.name() = map(StringBiDiMappings.name())

fun StringBiDiMappings.name() = BiDiMapping(::Name, Name::value)