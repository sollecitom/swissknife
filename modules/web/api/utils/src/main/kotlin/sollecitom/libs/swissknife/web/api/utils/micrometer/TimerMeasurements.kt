package sollecitom.libs.swissknife.web.api.utils.micrometer

import io.micrometer.core.instrument.Meter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import kotlin.time.Duration

// TODO move this package into its own micrometer-utils module, as you'll have to use this for event processors as well
data class TimerMeasurements(val count: Long, val totalTime: Duration, val maxTime: Duration)

fun MeterRegistry.timersWithName(name: String) = meters.filterIsInstance<Timer>().filter { it.id.type == Meter.Type.TIMER && it.id.name == name }