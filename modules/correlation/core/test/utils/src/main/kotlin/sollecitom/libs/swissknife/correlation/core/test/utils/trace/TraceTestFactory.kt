package sollecitom.libs.swissknife.correlation.core.test.utils.trace

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.SortableTimestampedUniqueIdentifier
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.trace.ExternalInvocationTrace
import sollecitom.libs.swissknife.correlation.core.domain.trace.InvocationTrace
import sollecitom.libs.swissknife.correlation.core.domain.trace.Trace
import kotlinx.datetime.Instant
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import kotlin.time.Duration.Companion.seconds

context(time: TimeGenerator)
fun Trace.ElapsedTimeSelector.sinceInvocationStarted() = sinceInvocationStarted(time.now())

context(time: TimeGenerator)
fun Trace.ElapsedTimeSelector.sinceParentInvocationStarted() = sinceParentInvocationStarted(time.now())

context(time: TimeGenerator)
fun Trace.ElapsedTimeSelector.sinceOriginatingInvocationStarted() = sinceOriginatingInvocationStarted(time.now())

context(ids: UniqueIdGenerator, time: TimeGenerator)
fun InvocationTrace.Companion.create(id: SortableTimestampedUniqueIdentifier<*> = ids.newId(), createdAt: Instant = time.now()) = InvocationTrace(id, createdAt)

context(ids: UniqueIdGenerator)
fun ExternalInvocationTrace.Companion.create(invocationId: Id = ids.newId.external(), actionId: Id = ids.newId.external()) = ExternalInvocationTrace(invocationId = invocationId, actionId = actionId)

context(time: TimeGenerator, ids: UniqueIdGenerator)
fun Trace.Companion.create(timeNow: Instant = time.now(), externalInvocationTrace: ExternalInvocationTrace = ExternalInvocationTrace.create(), originatingTrace: InvocationTrace = timeNow.minus(3.seconds).let { InvocationTrace.create(id = ids.newId.internal(it), createdAt = it) }, parentTrace: InvocationTrace = timeNow.minus(2.seconds).let { InvocationTrace.create(id = ids.newId.internal(it), createdAt = it) }, invocationId: SortableTimestampedUniqueIdentifier<*> = ids.newId.internal(timeNow)): Trace = Trace(invocation = InvocationTrace.create(id = invocationId, createdAt = timeNow), parent = parentTrace, originating = originatingTrace, external = externalInvocationTrace)