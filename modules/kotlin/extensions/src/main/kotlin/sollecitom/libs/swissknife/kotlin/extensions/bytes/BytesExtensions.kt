package sollecitom.libs.swissknife.kotlin.extensions.bytes

import sollecitom.libs.swissknife.kotlin.extensions.number.roundToCeil
import kotlin.math.log2

val Int.requiredBits: Int get() = if (this == 0) 0 else log2(toDouble()).roundToCeil()