package sollecitom.libs.swissknife.core.domain.time

import sollecitom.libs.swissknife.kotlin.extensions.time.localDate
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import java.time.Year
import java.time.format.TextStyle
import java.util.*

data class MonthAndYear(val month: Month, val year: Year) : Comparable<MonthAndYear> {

    fun displayName(style: TextStyle = TextStyle.NARROW, locale: Locale = Locale.UK) = "${month.getDisplayName(style, locale)}/${year.value}"

    override fun compareTo(other: MonthAndYear) = comparator.compare(this, other)

    companion object {
        private val comparator = Comparator.comparing(MonthAndYear::year).thenComparing(MonthAndYear::month)
    }
}

fun Clock.monthAndYear(timeZone: TimeZone = TimeZone.currentSystemDefault()): MonthAndYear = localDate(timeZone).monthAndYear
val LocalDate.monthAndYear: MonthAndYear get() = MonthAndYear(month = month, year = year.let(Year::of))