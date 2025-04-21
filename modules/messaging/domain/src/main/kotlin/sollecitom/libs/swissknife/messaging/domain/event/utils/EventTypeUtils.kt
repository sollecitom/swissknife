package sollecitom.libs.swissknife.messaging.domain.event.utils

import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.logger.core.Logger
import sollecitom.libs.swissknife.logger.core.loggable.Loggable
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.domain.message.properties.MessagePropertyNames
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

context(MessagePropertyNames)
fun <EVENT : Event> Message<EVENT>.eventType(): Happening.Type {

    val rawType = properties[forEvents.type]!!
    return Happening.Type.parse(rawType)
}

context(MessagePropertyNames)
fun <EVENT : Event> Message<EVENT>.hasType(type: Happening.Type): Boolean = eventType() == type

context(MessagePropertyNames)
fun <EVENT : Event> Flow<ReceivedMessage<EVENT>>.skipIfTypeIsNot(type: Happening.Type, logger: Logger) = onlyWithTypeIn(setOf(type), logger)

context(MessagePropertyNames)
fun <EVENT : Event> Flow<ReceivedMessage<EVENT>>.onlyWithTypeIn(types: Set<Happening.Type>, logger: Logger) = filter {

    val needsProcessing = it.eventType() in types
    if (!needsProcessing) {
        logger.debug { "Skipped message with ID '${it.id.stringRepresentation}' and type '${it.eventType().stringValue}', as its type is not included in $types" }
        it.acknowledge()
        logger.debug { "Skipped message with ID '${it.id.stringRepresentation}' and type '${it.eventType().stringValue}', as its type is not included in $types" }
    }
    needsProcessing
}

context(MessagePropertyNames)
@Suppress("UNCHECKED_CAST")
fun <EVENT : Event, SUB_TYPE : EVENT> Flow<ReceivedMessage<EVENT>>.onlyWithType(type: Happening.Type, logger: Logger): Flow<ReceivedMessage<SUB_TYPE>> {

    return skipIfTypeIsNot(type, logger).map { it as ReceivedMessage<SUB_TYPE> }
}

context(MessagePropertyNames, Loggable)
fun <EVENT : Event> Flow<ReceivedMessage<EVENT>>.onlyWithTypeIn(types: Set<Happening.Type>) = onlyWithTypeIn(types, logger)

context(MessagePropertyNames, Loggable)
fun <EVENT : Event> Flow<ReceivedMessage<EVENT>>.onlyWithTypeIn(type: Happening.Type, vararg otherTypes: Happening.Type) = onlyWithTypeIn(setOf(type) + otherTypes)

context(MessagePropertyNames, Loggable)
fun <EVENT : Event, SUB_TYPE : EVENT> Flow<ReceivedMessage<EVENT>>.onlyWithType(type: Happening.Type) = onlyWithType<EVENT, SUB_TYPE>(type, logger)