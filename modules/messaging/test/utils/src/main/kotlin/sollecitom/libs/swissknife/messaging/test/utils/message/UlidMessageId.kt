package sollecitom.libs.swissknife.messaging.test.utils.message

import sollecitom.libs.swissknife.core.domain.identity.ULID
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator
import sollecitom.libs.swissknife.messaging.domain.message.Message
import sollecitom.libs.swissknife.messaging.domain.topic.Topic

private data class UlidMessageId(val ulid: ULID, override val topic: Topic, override val partition: Topic.Partition?) : Message.Id {

    override val stringRepresentation get() = ulid.stringValue

    override fun compareTo(other: Message.Id): Int {

        require(other is UlidMessageId) { "Cannot compare message IDs of different kinds" }
        return ulid.compareTo(other.ulid)
    }
}

context(ids: UniqueIdGenerator)
fun Message.Id.Companion.ulid(topic: Topic, value: ULID = ids.newId.ulid.monotonic(), partition: Topic.Partition? = null): Message.Id = UlidMessageId(ulid = value, topic = topic, partition = partition)