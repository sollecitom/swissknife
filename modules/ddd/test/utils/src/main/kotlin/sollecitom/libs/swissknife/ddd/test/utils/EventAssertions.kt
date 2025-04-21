package sollecitom.libs.swissknife.ddd.test.utils

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening

fun Assert<Event>.hasInvocationContext(invocationContext: InvocationContext<*>) = given { event ->

    assertThat(event.context.invocation).isEqualTo(invocationContext)
}

fun <EVENT : Event> Assert<EVENT>.hasType(type: Happening.Type) = given { event ->

    assertThat(event.type).isEqualTo(type)
}

context(context: InvocationContext<*>)
fun Assert<Event>.hasTheInvocationContextInScope() = hasInvocationContext(context)

context(expected: InvocationContext<*>)
fun Assert<Event>.hasTheForkedInvocationContextInScope() = given { event ->

    assertThat(event.context.invocation).isEqualTo(expected.fork(invocation = event.context.invocation.trace.invocation))
}

fun Assert<Event>.isOriginating() = given { event ->

    assertThat(event.context.isOriginating).isTrue()
}

fun Assert<Event>.descendsFrom(parent: Event) = given { event ->

    assertThat(event.context.parent).isEqualTo(parent.reference)
}

fun Assert<Event>.originatesFrom(originating: Event) = given { event ->

    assertThat(event.context.originating).isEqualTo(originating.reference)
}

fun Assert<Event>.descendsAndOriginatesFrom(parent: Event) = given { event ->

    assertThat(event).descendsFrom(parent)
    assertThat(event).originatesFrom(parent)
}

context(context: InvocationContext<*>)
fun Assert<Event>.isCorrelatedWithTheInvocationContextInScope() = given { event ->

    assertThat(event).hasInvocationContext(context)
}