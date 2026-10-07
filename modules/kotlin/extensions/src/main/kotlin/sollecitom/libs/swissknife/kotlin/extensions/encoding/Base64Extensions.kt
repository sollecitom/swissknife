package sollecitom.libs.swissknife.kotlin.extensions.encoding

import java.util.*

fun String.base64Encoded(): String = toByteArray().let(Base64.getEncoder()::encodeToString)
fun String.base64EncodedAsByteArray(): ByteArray = toByteArray().base64Encoded()
fun ByteArray.base64Encoded(): ByteArray = Base64.getEncoder().encode(this)

fun String.base64Decoded(): String = Base64.getDecoder().decode(this).let(::String)
fun ByteArray.base64Decoded(): ByteArray = Base64.getDecoder().decode(this)
fun ByteArray.base64DecodedAsString(): String = Base64.getDecoder().decode(this).let(::String)