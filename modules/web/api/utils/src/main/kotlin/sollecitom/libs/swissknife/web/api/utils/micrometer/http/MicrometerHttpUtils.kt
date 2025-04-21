package sollecitom.libs.swissknife.web.api.utils.micrometer.http

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Statistic
import sollecitom.libs.swissknife.web.api.utils.micrometer.TimerMeasurements
import sollecitom.libs.swissknife.web.api.utils.micrometer.timersWithName
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.seconds

val MeterRegistry.httpServerRequestLatencyMeasurements: TimerMeasurements
    get() {

        val serverRequestLatencyTimer = timersWithName(HTTP4K_SERVER_REQUEST_LATENCY_NAME).single()
        val measurements = serverRequestLatencyTimer.measure().toList()
        check(serverRequestLatencyTimer.baseTimeUnit() == TimeUnit.SECONDS) { "This only works with timers whose base time unit is SECONDS. Improve this if you want to support other time units." }
        val count = measurements.single { it.statistic == Statistic.COUNT }.value.toLong()
        val totalTime = measurements.single { it.statistic == Statistic.TOTAL_TIME }.value.seconds
        val maxTime = measurements.single { it.statistic == Statistic.MAX }.value.seconds
        return TimerMeasurements(count = count, totalTime = totalTime, maxTime = maxTime)
    }

private const val HTTP4K_SERVER_REQUEST_LATENCY_NAME = "http.server.request.latency"