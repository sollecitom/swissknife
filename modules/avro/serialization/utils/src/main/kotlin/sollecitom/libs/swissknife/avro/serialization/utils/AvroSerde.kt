package sollecitom.libs.swissknife.avro.serialization.utils

interface AvroSerde<VALUE : Any> : AvroSerializer<VALUE>, AvroDeserializer<VALUE>