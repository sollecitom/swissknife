package sollecitom.libs.swissknife.messaging.domain.event.processing

import assertk.assertThat
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.domain.versioning.IntVersion
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.context.InvocationContext
import sollecitom.libs.swissknife.correlation.core.test.utils.context.unauthenticated
import sollecitom.libs.swissknife.ddd.domain.Event
import sollecitom.libs.swissknife.ddd.domain.Happening
import sollecitom.libs.swissknife.ddd.test.utils.create
import sollecitom.libs.swissknife.messaging.domain.message.ReceivedMessage
import sollecitom.libs.swissknife.messaging.test.utils.message.inMemorySpy
import kotlin.coroutines.cancellation.CancellationException

@TestInstance(PER_CLASS)
class CompositeEventProcessingUtilsTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `cancellation while processing propagates instead of becoming a failure`() {

        val message = ReceivedMessage.inMemorySpy(value = Event.Composite(data = TestData, metadata = Event.Metadata.create()))

        val result = with(InvocationContext.unauthenticated()) {
            runCatching { message.processAsCompositeEvent { _, _ -> throw CancellationException("stopped") } }
        }

        assertThat(result).isFailure().isInstanceOf(CancellationException::class)
    }

    private data object TestData : Event.Data {

        override val type = Happening.Type(Name("test-data"), IntVersion(1))
    }
}
