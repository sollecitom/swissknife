package sollecitom.libs.swissknife.ddd.test.utils

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import kotlinx.datetime.Instant

context(time: TimeGenerator, ids: UniqueIdGenerator)
fun Event.Metadata.Companion.create(id: Id = ids.newId.external(), timestamp: Instant = time.now(), context: Event.Context = Event.Context.create()) = Event.Metadata(id, timestamp, context)

context(time: TimeGenerator, ids: UniqueIdGenerator, context: InvocationContext<*>)
fun Event.Metadata.Companion.createWithContext(id: Id = ids.newId.external(), timestamp: Instant = time.now(), parentEventReference: Event.Reference? = null, originatingEventReference: Event.Reference? = null) = Event.Metadata(id, timestamp, Event.Context.create(invocation = context, parent = parentEventReference, originating = originatingEventReference))