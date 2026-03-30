package sollecitom.libs.swissknife.ddd.domain.hexagonal

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.core.domain.networking.Port

/** An adapter that drives the application (e.g. HTTP endpoint, message consumer). */
interface DrivingAdapter : Startable, Stoppable {

    /** A [DrivingAdapter] that exposes a network port. */
    interface WithPortBinding : DrivingAdapter {

        val port: Port
    }
}