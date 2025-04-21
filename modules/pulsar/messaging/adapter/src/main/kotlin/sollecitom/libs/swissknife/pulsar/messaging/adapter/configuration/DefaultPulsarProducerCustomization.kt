package sollecitom.libs.swissknife.pulsar.messaging.adapter.configuration

import org.apache.pulsar.client.api.CompressionType
import org.apache.pulsar.client.api.HashingScheme
import org.apache.pulsar.client.api.ProducerBuilder

fun <VALUE> ProducerBuilder<VALUE>.defaultProducerCustomization(): ProducerBuilder<VALUE> {

    return enableBatching(false)
        .enableChunking(true)
        .autoUpdatePartitions(true)
        .compressionType(CompressionType.ZLIB)
        .hashingScheme(HashingScheme.Murmur3_32Hash)
}