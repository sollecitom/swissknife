package sollecitom.libs.swissknife.ddd.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.text.Name

interface EntityEvent : Event {

    val entityId: Id
    val entityType: Name

    companion object
}