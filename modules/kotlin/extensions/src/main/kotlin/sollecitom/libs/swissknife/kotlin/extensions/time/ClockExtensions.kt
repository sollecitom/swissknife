package sollecitom.libs.swissknife.kotlin.extensions.time

import kotlinx.datetime.*
import java.time.ZoneId
import java.time.Clock as JavaClock

fun Clock.localDateTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime = now().toLocalDateTime(timeZone)

fun Clock.localDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate = localDateTime(timeZone).date

fun Clock.localTime(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalTime = localDateTime(timeZone).time

val Clock.localDateTime: LocalDateTime get() = localDateTime()

val Clock.localDate: LocalDate get() = localDate()

val Clock.localTime: LocalTime get() = localTime()

fun Clock.Companion.fixed(instant: Instant): Clock = FixedInstantClock(instant)

fun Clock.toJavaClock(): JavaClock = JavaClockAdapter(clock = this)

private class FixedInstantClock(val instant: Instant) : Clock {

    override fun now() = instant
}

private class JavaClockAdapter(private val clock: Clock, private val zone: ZoneId = ZoneId.systemDefault()) : JavaClock() {

    override fun getZone() = zone

    override fun withZone(zone: ZoneId): JavaClock = JavaClockAdapter(clock, zone)

    override fun instant() = clock.now().toJavaInstant()
}