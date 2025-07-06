package sollecitom.libs.swissknife.kotlin.extensions.time

import kotlinx.datetime.DatePeriod
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import java.time.temporal.ChronoUnit
import kotlin.time.toKotlinInstant

fun Instant.truncatedToMilliseconds(): Instant = toJavaInstant().truncatedTo(ChronoUnit.MILLIS).toKotlinInstant()

fun Instant.truncatedToSeconds(): Instant = toJavaInstant().truncatedTo(ChronoUnit.SECONDS).toKotlinInstant()

val Int.years: DatePeriod get() = DatePeriod(years = this)

val Int.months: DatePeriod get() = DatePeriod(months = this)