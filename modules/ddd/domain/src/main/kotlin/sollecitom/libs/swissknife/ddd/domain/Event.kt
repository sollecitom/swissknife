package sollecitom.libs.swissknife.ddd.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.traits.Contextual
import sollecitom.libs.swissknife.core.domain.traits.Identifiable
import sollecitom.libs.swissknife.core.domain.traits.Timestamped
import sollecitom.libs.swissknife.core.utils.TimeGenerator
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.Access
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.domain.context.externalActionId
import sollecitom.libs.swissknife.correlation.core.domain.context.externalInvocationId
import sollecitom.libs.swissknife.correlation.core.domain.context.wasTriggeredByInvocationId
import sollecitom.libs.swissknife.correlation.core.domain.trace.InvocationTrace
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import kotlinx.datetime.Instant
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke

interface Event : Happening, Identifiable, Timestamped, Contextual<Event.Context> {

    val reference: Reference get() = Reference(id, type, timestamp)

    fun forkContext(invocation: InvocationTrace): Context = context.fork(invocationTrace = invocation, parent = reference)

    data class Metadata(override val id: Id, override val timestamp: Instant, override val context: Context) : Identifiable, Timestamped, Contextual<Context> {

        companion object
    }

    interface Data {

        val type: Happening.Type
    }

    data class Composite<out DATA : Data>(val data: DATA, val metadata: Metadata) : Event {

        constructor(data: DATA, id: Id, timestamp: Instant, context: Context) : this(data = data, metadata = Metadata(id, timestamp, context))

        override val id: Id get() = metadata.id
        override val timestamp: Instant get() = metadata.timestamp
        override val context: Context get() = metadata.context
        override val type: Happening.Type get() = data.type

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Event) return false
            if (id != other.id) return false
            if (type != other.type) return false
            return true
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + type.hashCode()
            return result
        }

        override fun toString() = "Event.Composite(data=$data, metadata=$metadata)"

        companion object
    }

    data class Reference(override val id: Id, val type: Happening.Type, override val timestamp: Instant) : Timestamped, Identifiable {

        companion object
    }

    data class Context(val invocation: InvocationContext<Access>, val parent: Reference? = null, val originating: Reference? = null) {

        init {
            require(parent == null && originating == null || parent != null && originating != null) { "Parent and originating event references must both be either specified or omitted" }
        }

        fun fork(invocationTrace: InvocationTrace, parent: Reference?) = copy(invocation = invocation.fork(invocationTrace), parent = parent, originating = originating ?: parent)

        val isOriginating: Boolean get() = originating == null

        companion object
    }

    companion object
}

val Event.isOriginating: Boolean get() = context.isOriginating

context(context: InvocationContext<*>)
fun Event.forkContext(): Event.Context = forkContext(invocation = context.trace.invocation)

context(ids: UniqueIdGenerator, time: TimeGenerator)
fun Event.forkContext(): Event.Context = forkContext(invocation = InvocationTrace(id = ids.newId(), createdAt = time.now()))

context(_: UniqueIdGenerator, _: TimeGenerator)
fun Event.forkInvocationContext() = forkContext().invocation

fun InvocationContext<*>.toEventContext(parentEvent: Event.Reference? = null, originatingEvent: Event.Reference? = null): Event.Context = Event.Context(this, parentEvent, originatingEvent)

fun Event.descendsFromEventWithReference(reference: Event.Reference) = context.parent == reference
fun Event.descendsFrom(event: Event) = descendsFromEventWithReference(event.reference)

fun Event.originatesFromEventWithReference(reference: Event.Reference) = context.originating == reference
fun Event.originatesFrom(event: Event) = originatesFromEventWithReference(event.reference)

fun <DATA : Event.Data> DATA.asEvent(metadata: Event.Metadata): Event.Composite<DATA> = Event.Composite(data = this, metadata = metadata)

context(_: UniqueIdGenerator, _: TimeGenerator, loggable: Loggable)
fun Event.forkAndLogInvocationContext(): InvocationContext<*> {

    val forkedContext = forkInvocationContext()
    loggable.logger.debug { "Forked invocation context $forkedContext from event context $context." }
    return forkedContext
}

val Event.externalInvocationId get() = context.externalInvocationId
val Event.externalActionId get() = context.externalActionId
fun Event.wasTriggeredByInvocationId(externalInvocationId: Id) = context.wasTriggeredByInvocationId(externalInvocationId)

val Event.Context.externalInvocationId get() = invocation.externalInvocationId
val Event.Context.externalActionId get() = invocation.externalActionId
fun Event.Context.wasTriggeredByInvocationId(externalInvocationId: Id) = invocation.wasTriggeredByInvocationId(externalInvocationId)
fun Event.wasCausedBy(event: Event) = wasTriggeredByInvocationId(event.externalInvocationId)