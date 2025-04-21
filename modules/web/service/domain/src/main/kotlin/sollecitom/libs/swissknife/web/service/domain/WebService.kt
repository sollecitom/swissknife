package sollecitom.libs.swissknife.web.service.domain

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable

interface WebService : WithWebInterface, Startable, Stoppable