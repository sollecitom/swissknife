package sollecitom.libs.swissknife.core.utils

import kotlinx.datetime.Clock
import java.time.Clock as JavaClock

interface TimeGenerator {

    val clock: Clock
    val javaClock: JavaClock

    fun now() = clock.now()
}