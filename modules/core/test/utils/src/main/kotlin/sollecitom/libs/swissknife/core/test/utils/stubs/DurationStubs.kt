package sollecitom.libs.swissknife.core.test.utils.stubs

import sollecitom.libs.swissknife.core.utils.TimeGenerator
import kotlinx.datetime.Instant
import kotlin.time.Duration

context(TimeGenerator)
val Duration.ago: Instant
    get() = clock.now() - this

context(TimeGenerator)
val Duration.fromNow: Instant
    get() = clock.now() + this