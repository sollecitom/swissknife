package sollecitom.libs.swissknife.correlation.core.test.utils.context

import assertk.Assert
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext

fun Assert<InvocationContext<*>>.wasForkedFrom(invocationContext: InvocationContext<*>) = given { context ->

    assertThat(context).isEqualTo(invocationContext.fork(context.trace.invocation))
}