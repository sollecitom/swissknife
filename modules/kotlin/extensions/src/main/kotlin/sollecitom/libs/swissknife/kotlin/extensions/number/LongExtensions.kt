package sollecitom.libs.swissknife.kotlin.extensions.number

import java.nio.ByteBuffer

fun Long.toByteArray(): ByteArray = ByteBuffer.wrap(ByteArray(Long.SIZE_BYTES)).putLong(this).array()