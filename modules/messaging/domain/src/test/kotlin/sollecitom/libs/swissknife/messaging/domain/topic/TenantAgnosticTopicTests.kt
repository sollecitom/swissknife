package sollecitom.libs.swissknife.messaging.domain.topic

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isFailure
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import sollecitom.libs.swissknife.core.domain.text.Name
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class TenantAgnosticTopicTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class Creation {

        @Test
        fun `creating a persistent tenant-agnostic topic`() {

            val topic = TenantAgnosticTopic.persistent(Name("my-namespace"), Name("my-topic"))

            assertThat(topic.persistent).isTrue()
            assertThat(topic.name).isEqualTo(Name("my-topic"))
            assertThat(topic.namespaceName).isEqualTo(Name("my-namespace"))
        }

        @Test
        fun `creating a non-persistent tenant-agnostic topic`() {

            val topic = TenantAgnosticTopic.nonPersistent(Name("my-namespace"), Name("my-topic"))

            assertThat(topic.persistent).isFalse()
        }

        @Test
        fun `full name of a persistent tenant-agnostic topic`() {

            val topic = TenantAgnosticTopic.persistent(Name("ns1"), Name("events"))

            assertThat(topic.fullName.value).isEqualTo("persistent://ns1/events")
        }

        @Test
        fun `full name of a non-persistent tenant-agnostic topic`() {

            val topic = TenantAgnosticTopic.nonPersistent(Name("ns1"), Name("events"))

            assertThat(topic.fullName.value).isEqualTo("non-persistent://ns1/events")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class WithTenant {

        @Test
        fun `adding a tenant to a persistent topic`() {

            val agnosticTopic = TenantAgnosticTopic.persistent(Name("ns1"), Name("orders"))
            val tenant = Name("tenant-a")

            val topic = agnosticTopic.withTenant(tenant)

            assertThat(topic.persistent).isTrue()
            assertThat(topic.name).isEqualTo(Name("orders"))
            assertThat(topic.namespace).isNotNull()
            assertThat(topic.namespace?.tenant).isEqualTo(tenant)
            assertThat(topic.namespace?.name).isEqualTo(Name("ns1"))
        }

        @Test
        fun `adding a tenant to a non-persistent topic`() {

            val agnosticTopic = TenantAgnosticTopic.nonPersistent(Name("ns1"), Name("orders"))
            val tenant = Name("tenant-b")

            val topic = agnosticTopic.withTenant(tenant)

            assertThat(topic.persistent).isFalse()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Parsing {

        @Test
        fun `parsing a valid persistent tenant-agnostic topic`() {

            val rawTopic = "persistent://my-namespace/my-topic"

            val topic = TenantAgnosticTopic.parse(rawTopic)

            assertThat(topic.persistent).isTrue()
            assertThat(topic.namespaceName).isEqualTo(Name("my-namespace"))
            assertThat(topic.name).isEqualTo(Name("my-topic"))
        }

        @Test
        fun `parsing a valid non-persistent tenant-agnostic topic`() {

            val rawTopic = "non-persistent://my-namespace/my-topic"

            val topic = TenantAgnosticTopic.parse(rawTopic)

            assertThat(topic.persistent).isFalse()
        }

        @Test
        fun `parsing an invalid format`() {

            val result = runCatching { TenantAgnosticTopic.parse("invalid") }

            assertThat(result).isFailure().isInstanceOf<IllegalStateException>()
        }

        @Test
        fun `parsing with too many parts`() {

            val result = runCatching { TenantAgnosticTopic.parse("persistent://a/b/c/d/e") }

            assertThat(result).isFailure().isInstanceOf<IllegalArgumentException>()
        }
    }
}
