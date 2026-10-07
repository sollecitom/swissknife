package sollecitom.libs.swissknife.pulsar.messaing.test.utils

import sollecitom.libs.swissknife.messaging.domain.topic.Topic
import sollecitom.libs.swissknife.pulsar.utils.registerSchema
import org.apache.avro.Schema
import org.apache.pulsar.client.admin.PulsarAdmin

fun PulsarAdmin.registerSchema(topic: Topic, schema: Schema) = registerSchema(topic.fullName.value, schema)
