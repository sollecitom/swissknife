package sollecitom.libs.swissknife.core.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import sollecitom.libs.swissknife.core.domain.time.monthAndYear
import kotlin.time.Clock
import java.time.Clock as JavaClock

interface TimeGenerator {

    val clock: Clock
    val javaClock: JavaClock

    fun now() = clock.now()
}

fun TimeGenerator.monthAndYear(timeZone: TimeZone = TimeZone.currentSystemDefault()): YearMonth = clock.monthAndYear(timeZone)