package sollecitom.libs.swissknife.core.test.utils.stubs

import sollecitom.libs.swissknife.core.utils.TimeGenerator
import kotlinx.datetime.Instant
import kotlin.time.Duration

context(generator: TimeGenerator)
val Duration.ago: Instant
    get() = generator.now() - this

context(generator: TimeGenerator)
val Duration.fromNow: Instant
    get() = generator.now() + this