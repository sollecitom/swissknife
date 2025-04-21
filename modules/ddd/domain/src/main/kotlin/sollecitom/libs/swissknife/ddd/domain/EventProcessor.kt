package sollecitom.libs.swissknife.ddd.domain

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable

interface EventProcessor : Startable, Stoppable {

    companion object
}