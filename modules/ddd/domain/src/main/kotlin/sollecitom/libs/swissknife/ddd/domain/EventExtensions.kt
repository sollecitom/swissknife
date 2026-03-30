package sollecitom.libs.swissknife.ddd.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance

/** Filters a flow of events to only [EntityEvent]s belonging to the given [entityId]. */
fun Flow<Event>.filterIsForEntityId(entityId: Id): Flow<EntityEvent> = filterIsInstance<EntityEvent>().filter { it.entityId == entityId }