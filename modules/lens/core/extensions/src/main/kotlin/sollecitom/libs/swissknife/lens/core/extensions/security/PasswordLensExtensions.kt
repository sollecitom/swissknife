package sollecitom.libs.swissknife.lens.core.extensions.security

import sollecitom.libs.swissknife.core.domain.security.Password
import sollecitom.libs.swissknife.lens.core.extensions.map
import org.http4k.lens.*

fun Path.password() = map(StringBiDiMappings.password())

fun <IN : Any> BiDiLensSpec<IN, String>.password() = map(StringBiDiMappings.password())

fun StringBiDiMappings.password() = BiDiMapping(::Password, Password::value)