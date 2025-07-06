package sollecitom.libs.swissknife.kotlin.extensions.time

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.YearMonth
import kotlinx.datetime.toJavaMonth
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

fun Instant.truncatedToMilliseconds(): Instant = toJavaInstant().truncatedTo(ChronoUnit.MILLIS).toKotlinInstant()

fun Instant.truncatedToSeconds(): Instant = toJavaInstant().truncatedTo(ChronoUnit.SECONDS).toKotlinInstant()

val Int.years: DatePeriod get() = DatePeriod(years = this)

val Int.months: DatePeriod get() = DatePeriod(months = this)

fun YearMonth.displayName(style: TextStyle = TextStyle.SHORT, locale: Locale = Locale.getDefault()) = "${month.toJavaMonth().getDisplayName(style, locale)} $year"