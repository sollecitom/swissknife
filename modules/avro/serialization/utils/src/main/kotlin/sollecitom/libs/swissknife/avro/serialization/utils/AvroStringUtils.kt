package sollecitom.libs.swissknife.avro.serialization.utils

import org.apache.avro.util.Utf8

internal fun Any.asString(): String = if (this is Utf8) toString() else this as String