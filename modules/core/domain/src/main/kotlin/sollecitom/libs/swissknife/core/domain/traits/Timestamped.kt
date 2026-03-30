package sollecitom.libs.swissknife.core.domain.traits

import kotlin.time.Instant

/** A type that carries a point-in-time timestamp. */
interface Timestamped {

    val timestamp: Instant
}