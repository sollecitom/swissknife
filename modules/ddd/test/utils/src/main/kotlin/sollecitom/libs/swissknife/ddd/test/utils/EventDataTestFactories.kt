package sollecitom.libs.swissknife.ddd.test.utils

import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.asEvent

context(_: UniqueIdGenerator, _: TimeGenerator)
fun <DATA : Event.Data> DATA.asEvent(metadata: Event.Metadata = Event.Metadata.create()) = asEvent(metadata)

context(_: UniqueIdGenerator, _: TimeGenerator, _: InvocationContext<*>)
fun <DATA : Event.Data> DATA.asEventWithContext(metadata: Event.Metadata = Event.Metadata.createWithContext()) = asEvent(metadata)