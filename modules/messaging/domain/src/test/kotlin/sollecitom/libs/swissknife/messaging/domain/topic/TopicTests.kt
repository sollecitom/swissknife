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
class TopicTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class PersistentTopic {

        @Test
        fun `creating a persistent topic`() {

            val name = Name("orders")
            val topic = Topic.persistent(name)

            assertThat(topic.persistent).isTrue()
            assertThat(topic.name).isEqualTo(name)
            assertThat(topic.namespace).isEqualTo(Topic.Namespace.default)
        }

        @Test
        fun `creating a persistent topic with namespace`() {

            val name = Name("orders")
            val namespace = Topic.Namespace(Name("my-tenant"), Name("my-namespace"))
            val topic = Topic.persistent(name, namespace)

            assertThat(topic.persistent).isTrue()
            assertThat(topic.namespace).isEqualTo(namespace)
        }

        @Test
        fun `a persistent topic without namespace is in public default`() {

            val topic = Topic.persistent(Name("orders"))

            assertThat(topic.fullName.value).isEqualTo("persistent://public/default/orders")
        }

        @Test
        fun `a topic without namespace round-trips through its full name`() {

            val topic = Topic.persistent(Name("orders"))

            val parsed = Topic.parse(topic.fullName.value)

            assertThat(parsed).isEqualTo(topic)
        }

        @Test
        fun `persistent topic full name with namespace`() {

            val namespace = Topic.Namespace(Name("tenant1"), Name("ns1"))
            val topic = Topic.persistent(Name("orders"), namespace)

            assertThat(topic.fullName.value).isEqualTo("persistent://tenant1/ns1/orders")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class NonPersistentTopic {

        @Test
        fun `creating a non-persistent topic`() {

            val name = Name("events")
            val topic = Topic.nonPersistent(name)

            assertThat(topic.persistent).isFalse()
            assertThat(topic.name).isEqualTo(name)
        }

        @Test
        fun `non-persistent topic full name`() {

            val topic = Topic.nonPersistent(Name("events"))

            assertThat(topic.fullName.value).isEqualTo("non-persistent://public/default/events")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Parsing {

        @Test
        fun `parsing a persistent topic with namespace`() {

            val rawTopic = "persistent://my-tenant/my-namespace/my-topic"

            val topic = Topic.parse(rawTopic)

            assertThat(topic.persistent).isTrue()
            assertThat(topic.name).isEqualTo(Name("my-topic"))
            assertThat(topic.namespace).isNotNull()
            assertThat(topic.namespace.tenant).isEqualTo(Name("my-tenant"))
            assertThat(topic.namespace.name).isEqualTo(Name("my-namespace"))
        }

        @Test
        fun `parsing a non-persistent topic with namespace`() {

            val rawTopic = "non-persistent://my-tenant/my-namespace/my-topic"

            val topic = Topic.parse(rawTopic)

            assertThat(topic.persistent).isFalse()
            assertThat(topic.name).isEqualTo(Name("my-topic"))
        }

        @Test
        fun `parsing a topic with underscores in its tenant, namespace and name`() {

            val rawTopic = "persistent://my_tenant/my_namespace/my_topic"

            val topic = Topic.parse(rawTopic)

            assertThat(topic.name).isEqualTo(Name("my_topic"))
            assertThat(topic.namespace).isEqualTo(Topic.Namespace(Name("my_tenant"), Name("my_namespace")))
        }

        @Test
        fun `parsing a topic with trailing characters outside the allowed alphabet`() {

            val invalidTopic = "persistent://my-tenant/my-namespace/my-topic?"

            val result = runCatching { Topic.parse(invalidTopic) }

            assertThat(result).isFailure().isInstanceOf<IllegalStateException>()
        }

        @Test
        fun `parsing an invalid topic format`() {

            val invalidTopic = "not-a-valid-topic"

            val result = runCatching { Topic.parse(invalidTopic) }

            assertThat(result).isFailure().isInstanceOf<IllegalStateException>()
        }

        @Test
        fun `parsing a topic with too many parts`() {

            val invalidTopic = "persistent://a/b/c/d/e/f"

            val result = runCatching { Topic.parse(invalidTopic) }

            assertThat(result).isFailure().isInstanceOf<IllegalArgumentException>()
        }

        @Test
        fun `round-trip parsing of a persistent topic`() {

            val namespace = Topic.Namespace(Name("tenant1"), Name("ns1"))
            val original = Topic.persistent(Name("orders"), namespace)

            val parsed = Topic.parse(original.fullName.value)

            assertThat(parsed).isEqualTo(original)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class OfFactory {

        @Test
        fun `creating a topic with persistent protocol name`() {

            val topic = Topic.of(Topic.Persistent.protocol, name = Name("test"))

            assertThat(topic.persistent).isTrue()
        }

        @Test
        fun `creating a topic with non-persistent protocol name`() {

            val topic = Topic.of(Topic.NonPersistent.protocol, name = Name("test"))

            assertThat(topic.persistent).isFalse()
        }

        @Test
        fun `creating a topic with unknown protocol`() {

            val result = runCatching { Topic.of(Name("unknown"), name = Name("test")) }

            assertThat(result).isFailure().isInstanceOf<IllegalStateException>()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Partition {

        @Test
        fun `creating a valid partition`() {

            val partition = Topic.Partition(0)

            assertThat(partition.index).isEqualTo(0)
        }

        @Test
        fun `creating a partition with negative index`() {

            val result = runCatching { Topic.Partition(-1) }

            assertThat(result).isFailure().isInstanceOf<IllegalArgumentException>()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Equality {

        @Test
        fun `two topics with same properties are equal`() {

            val topic1 = Topic.persistent(Name("orders"))
            val topic2 = Topic.persistent(Name("orders"))

            assertThat(topic1).isEqualTo(topic2)
        }

        @Test
        fun `two topics with same properties have same hash code`() {

            val topic1 = Topic.persistent(Name("orders"))
            val topic2 = Topic.persistent(Name("orders"))

            assertThat(topic1.hashCode()).isEqualTo(topic2.hashCode())
        }
    }
}
