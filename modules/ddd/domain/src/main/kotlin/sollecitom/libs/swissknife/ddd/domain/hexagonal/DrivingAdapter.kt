package sollecitom.libs.swissknife.ddd.domain.hexagonal

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable
import sollecitom.libs.swissknife.core.domain.networking.Port

interface DrivingAdapter : Startable, Stoppable {

    interface WithPortBinding : DrivingAdapter {

        val port: Port
    }
}