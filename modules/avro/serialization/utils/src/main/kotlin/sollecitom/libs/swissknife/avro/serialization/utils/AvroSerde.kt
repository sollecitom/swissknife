package sollecitom.libs.swissknife.avro.serialization.utils

/** Combines [AvroSerializer] and [AvroDeserializer] for bidirectional Avro GenericRecord conversion. */
interface AvroSerde<VALUE : Any> : AvroSerializer<VALUE>, AvroDeserializer<VALUE>