package sollecitom.libs.swissknife.core.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import sollecitom.libs.swissknife.core.domain.time.MonthAndYear
import sollecitom.libs.swissknife.core.domain.time.monthAndYear
import sollecitom.libs.swissknife.kotlin.extensions.time.localDate
import java.time.Clock as JavaClock

interface TimeGenerator {

    val clock: Clock
    val javaClock: JavaClock

    fun now() = clock.now()
}

fun TimeGenerator.monthAndYear(timeZone: TimeZone = TimeZone.currentSystemDefault()): MonthAndYear = clock.monthAndYear(timeZone)