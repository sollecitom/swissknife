package sollecitom.libs.swissknife.core.domain.traits

import kotlinx.datetime.Instant

interface Timestamped {

    val timestamp: Instant
}