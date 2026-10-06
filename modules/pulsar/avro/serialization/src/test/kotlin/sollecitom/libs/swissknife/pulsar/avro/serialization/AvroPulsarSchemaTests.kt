package sollecitom.libs.swissknife.pulsar.avro.serialization

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.avro.generic.GenericRecordBuilder
import org.apache.pulsar.client.api.SubscriptionInitialPosition
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.pulsar.test.utils.client
import sollecitom.libs.swissknife.pulsar.test.utils.newPulsarContainer
import java.util.concurrent.TimeUnit

@TestInstance(PER_CLASS)
class AvroPulsarSchemaTests {

    private val pulsar = newPulsarContainer().apply { start() }
    private val client = pulsar.client()

    @Test
    fun `a message is read with the schema version it was written with`() {

        val topic = "persistent://public/default/things"
        val consumer = client.newConsumer(ThingV2Serde.asPulsarSchema()).topic(topic).subscriptionName("reader").subscriptionInitialPosition(SubscriptionInitialPosition.Earliest).subscribe()
        client.newProducer(ThingV1Serde.asPulsarSchema()).topic(topic).create().use { producer -> producer.send(ThingV1(name = "first")) }

        val received = consumer.receive(30, TimeUnit.SECONDS)

        assertThat(received.value).isEqualTo(ThingV2(name = "first", colour = "unknown"))
        consumer.close()
    }

    @AfterAll
    fun stopPulsar() {
        client.close()
        pulsar.stop()
    }

    private data class ThingV1(val name: String)

    private data class ThingV2(val name: String, val colour: String)

    private object ThingV1Serde : AvroSerde<ThingV1> {

        override val schema: Schema = Schema.Parser().parse("""{"type":"record","name":"Thing","namespace":"test","fields":[{"name":"name","type":"string"}]}""")

        override fun serialize(value: ThingV1): GenericRecord = GenericRecordBuilder(schema).set("name", value.name).build()

        override fun deserialize(value: GenericRecord) = ThingV1(name = value["name"].toString())
    }

    private object ThingV2Serde : AvroSerde<ThingV2> {

        override val schema: Schema = Schema.Parser().parse("""{"type":"record","name":"Thing","namespace":"test","fields":[{"name":"name","type":"string"},{"name":"colour","type":"string","default":"unknown"}]}""")

        override fun serialize(value: ThingV2): GenericRecord = GenericRecordBuilder(schema).set("name", value.name).set("colour", value.colour).build()

        override fun deserialize(value: GenericRecord) = ThingV2(name = value["name"].toString(), colour = value["colour"].toString())
    }
}
