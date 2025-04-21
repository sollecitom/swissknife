package sollecitom.libs.swissknife.web.api.test.utils

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry

interface LocalHttpDrivingAdapterTestSpecification {

    val meterRegistry: MeterRegistry get() = SimpleMeterRegistry()

    fun path(value: String) = "http://localhost:0/$value"
}