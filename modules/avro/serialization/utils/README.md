# Avro Serialization Utils

Avro serialization utilities: `AvroSerde`, `AvroSerializer`, `AvroDeserializer` interfaces, GenericRecord extensions, schema loading from resources, record builder DSL, and envelope field conventions.

The `GenericRecord` extension functions in `AvroGenericRecordExtensions.kt` use `!!` and unchecked casts. This is by design — they are intended for use inside `AvroDeserializer.deserialize()`, where Avro has already validated the payload against the schema. The schema guarantees fields exist with the correct types.
