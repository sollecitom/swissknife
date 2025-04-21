package sollecitom.libs.swissknife.kotlin.extensions.bytes

import sollecitom.libs.swissknife.kotlin.extensions.number.roundToCeil
import kotlin.math.log2

val Int.requiredBits: Int get() = log2(toDouble()).roundToCeil()