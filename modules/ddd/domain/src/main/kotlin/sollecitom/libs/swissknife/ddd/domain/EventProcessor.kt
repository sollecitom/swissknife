package sollecitom.libs.swissknife.ddd.domain

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable

/** A startable/stoppable component that processes domain events. */
interface EventProcessor : Startable, Stoppable {

    companion object
}