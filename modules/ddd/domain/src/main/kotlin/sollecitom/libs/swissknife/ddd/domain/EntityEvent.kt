package sollecitom.libs.swissknife.ddd.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.text.Name

/** An [Event] that is scoped to a specific entity, identified by [entityId] and [entityType]. */
interface EntityEvent : Event {

    val entityId: Id
    val entityType: Name

    companion object
}