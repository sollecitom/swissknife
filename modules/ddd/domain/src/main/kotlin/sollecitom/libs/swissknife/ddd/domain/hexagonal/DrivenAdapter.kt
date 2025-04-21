package sollecitom.libs.swissknife.ddd.domain.hexagonal

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable

interface DrivenAdapter : Startable, Stoppable