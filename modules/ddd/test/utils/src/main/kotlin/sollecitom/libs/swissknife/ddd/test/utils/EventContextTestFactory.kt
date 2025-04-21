package sollecitom.libs.swissknife.ddd.test.utils

import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.context.create
import sollecitom.libs.swissknife.ddd.domain.Event

context(_: UniqueIdGenerator, _: TimeGenerator)
fun Event.Context.Companion.create(invocation: InvocationContext<*> = InvocationContext.create(), parent: Event.Reference? = null, originating: Event.Reference? = null): Event.Context = Event.Context(invocation = invocation, parent = parent, originating = originating)