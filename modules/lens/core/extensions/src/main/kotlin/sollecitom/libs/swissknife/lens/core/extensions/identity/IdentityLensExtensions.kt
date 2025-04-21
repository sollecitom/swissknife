package sollecitom.libs.swissknife.lens.core.extensions.identity

import sollecitom.libs.swissknife.core.domain.identity.*
import sollecitom.libs.swissknife.lens.core.extensions.map
import org.http4k.lens.*

fun Path.ulid() = map(StringBiDiMappings.ulid())
fun Path.ksuid() = map(StringBiDiMappings.ksuid())
fun Path.stringId() = map(StringBiDiMappings.stringId())
fun <IN : Any> BiDiLensSpec<IN, String>.ulid() = map(StringBiDiMappings.ulid())
fun <IN : Any> BiDiLensSpec<IN, String>.ksuid() = map(StringBiDiMappings.ksuid())
fun <IN : Any> BiDiLensSpec<IN, String>.stringId() = map(StringBiDiMappings.stringId())
fun StringBiDiMappings.ulid() = BiDiMapping(ULID::invoke, ULID::stringValue)
fun StringBiDiMappings.ksuid() = BiDiMapping(KSUID::invoke, KSUID::stringValue)
fun StringBiDiMappings.stringId() = BiDiMapping(::StringId, StringId::stringValue)

fun Path.id() = map(StringBiDiMappings.id())
fun <IN : Any> BiDiLensSpec<IN, String>.id() = map(StringBiDiMappings.id())
fun StringBiDiMappings.id() = BiDiMapping(Id.Companion::fromString, Id::stringValue)