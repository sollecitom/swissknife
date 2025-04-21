package sollecitom.libs.swissknife.messaging.test.utils.context

import assertk.Assert
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.test.utils.descendsAndOriginatesFrom
import sollecitom.libs.swissknife.ddd.test.utils.descendsFrom
import sollecitom.libs.swissknife.ddd.test.utils.originatesFrom
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage

fun <EVENT : Event> Assert<List<EventAndParentContext<EVENT>>>.descendAndOriginateFrom(firstMessage: ReceivedMessage<Event>, vararg otherMessages: ReceivedMessage<Event>) = areCorrelatedWith(messages = listOf(firstMessage, *otherMessages)) { event, parentMessageContext, expectedParentMessage ->

    assertThat(event).descendsAndOriginatesFrom(expectedParentMessage.value)
    assertThat(parentMessageContext).isEqualTo(expectedParentMessage.context)
}

fun <EVENT : Event> Assert<List<EventAndParentContext<EVENT>>>.descendFrom(firstMessage: ReceivedMessage<Event>, vararg otherMessages: ReceivedMessage<Event>) = areCorrelatedWith(messages = listOf(firstMessage, *otherMessages)) { event, messageContext, expectedParentMessage ->

    assertThat(event).descendsFrom(expectedParentMessage.value)
    assertThat(messageContext.parentMessageId).isEqualTo(expectedParentMessage.id)
}

fun <EVENT : Event> Assert<List<EventAndParentContext<EVENT>>>.originateFrom(firstMessage: ReceivedMessage<Event>, vararg otherMessages: ReceivedMessage<Event>) = areCorrelatedWith(messages = listOf(firstMessage, *otherMessages)) { event, messageContext, expectedParentMessage ->

    assertThat(event).originatesFrom(expectedParentMessage.value)
    assertThat(messageContext.originatingMessageId).isEqualTo(expectedParentMessage.id)
}

private fun <EVENT : Event> Assert<List<EventAndParentContext<EVENT>>>.areCorrelatedWith(messages: List<ReceivedMessage<Event>>, assertion: (EVENT, Message.Context, ReceivedMessage<Event>) -> Unit) = given { events ->

    check(messages.size == events.size) { "Expected size ${events.size} for messages, but was ${messages.size}" }
    events.forEachIndexed { index, (event, messageContext) ->

        val expectedParentMessage = messages[index]
        assertion(event, messageContext, expectedParentMessage)
    }
}