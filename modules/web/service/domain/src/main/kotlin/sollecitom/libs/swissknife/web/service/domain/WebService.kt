package sollecitom.libs.swissknife.web.service.domain

import sollecitom.libs.swissknife.core.domain.lifecycle.Startable
import sollecitom.libs.swissknife.core.domain.lifecycle.Stoppable

/** A startable/stoppable service that exposes a web interface (main and health ports). */
interface WebService : WithWebInterface, Startable, Stoppable