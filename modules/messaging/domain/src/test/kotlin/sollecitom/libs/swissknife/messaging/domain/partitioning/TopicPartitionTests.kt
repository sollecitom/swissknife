package sollecitom.libs.swissknife.messaging.domain.partitioning

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.position.Index
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class TopicPartitionTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class Creation {

        @Test
        fun `creating a topic partition`() {

            val partition = TopicPartition("my-topic", Index(3))

            assertThat(partition.topic).isEqualTo("my-topic")
            assertThat(partition.partition).isEqualTo(Index(3))
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class PartitionAssignment {

        @Test
        fun `creating a partition assigned event`() {

            val topicPartition = TopicPartition("my-topic", Index(1))
            val assigned = PartitionAssigned(topicPartition, "consumer-1")

            assertThat(assigned.topicPartition).isEqualTo(topicPartition)
            assertThat(assigned.consumerName).isEqualTo("consumer-1")
        }

        @Test
        fun `creating a partition assigned event with convenience constructor`() {

            val assigned = PartitionAssigned(Index(2), "my-topic", "consumer-1")

            assertThat(assigned.topicPartition.topic).isEqualTo("my-topic")
            assertThat(assigned.topicPartition.partition).isEqualTo(Index(2))
        }

        @Test
        fun `creating a partition unassigned event`() {

            val topicPartition = TopicPartition("my-topic", Index(1))
            val unassigned = PartitionUnassigned(topicPartition, "consumer-1")

            assertThat(unassigned.topicPartition).isEqualTo(topicPartition)
            assertThat(unassigned.consumerName).isEqualTo("consumer-1")
        }

        @Test
        fun `creating a partition unassigned event with convenience constructor`() {

            val unassigned = PartitionUnassigned(Index(0), "my-topic", "consumer-2")

            assertThat(unassigned.topicPartition.topic).isEqualTo("my-topic")
            assertThat(unassigned.topicPartition.partition).isEqualTo(Index(0))
            assertThat(unassigned.consumerName).isEqualTo("consumer-2")
        }
    }
}
