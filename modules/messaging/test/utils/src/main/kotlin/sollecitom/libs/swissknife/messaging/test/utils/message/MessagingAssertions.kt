package sollecitom.libs.swissknife.messaging.test.utils.message

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import sollecitom.libs.swissknife.messaging.domain.message.Message

fun <VALUE> Assert<Message<VALUE>>.matches(outbound: Message<VALUE>, assertValue: (received: VALUE, original: VALUE) -> Unit = { received, original -> assertThat(received).isEqualTo(original) }) = given { received ->

    assertThat(received.key).isEqualTo(outbound.key)
    assertThat(received.properties).isEqualTo(outbound.properties)
    assertThat(received.context).isEqualTo(outbound.context)
    assertValue(received.value, outbound.value)
}

fun <VALUE> Assert<Message<VALUE>>.descendsFrom(parent: Message.Id) = given { message ->

    assertThat(message.context.parentMessageId).isNotNull().isEqualTo(parent)
}

fun <VALUE> Assert<Message<VALUE>>.originatesFrom(parent: Message.Id) = given { message ->

    assertThat(message.context.originatingMessageId).isNotNull().isEqualTo(parent)
}

fun <VALUE> Assert<Message<VALUE>>.descendsAndOriginatesFrom(messageId: Message.Id) = given { message ->

    assertThat(message).descendsFrom(messageId)
    assertThat(message).originatesFrom(messageId)
}

fun <VALUE> Assert<Message<VALUE>>.isOriginating() = given { message ->

    assertThat(message.context.parentMessageId).isNull()
    assertThat(message.context.originatingMessageId).isNull()
}