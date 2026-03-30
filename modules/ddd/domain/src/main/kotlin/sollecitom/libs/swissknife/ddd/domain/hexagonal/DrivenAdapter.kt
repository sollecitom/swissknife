package sollecitom.libs.swissknife.ddd.domain.hexagonal

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable

/** An infrastructure adapter that the application drives (e.g. database, message broker). */
interface DrivenAdapter : Startable, Stoppable