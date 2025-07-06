package sollecitom.libs.swissknife.core.domain.time

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import sollecitom.libs.swissknife.kotlin.extensions.time.localDate
import kotlin.time.Clock

fun Clock.monthAndYear(timeZone: TimeZone = TimeZone.currentSystemDefault()): YearMonth = localDate(timeZone).monthAndYear

val LocalDate.monthAndYear: YearMonth get() = YearMonth(year = year, month = month)