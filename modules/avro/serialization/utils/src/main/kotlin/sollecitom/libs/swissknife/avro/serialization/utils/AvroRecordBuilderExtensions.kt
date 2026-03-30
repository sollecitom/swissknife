package sollecitom.libs.swissknife.avro.serialization.utils

/** Sets a field by serializing [value] to a nested [GenericRecord] using the given [serializer]. */
fun <T : Any> AvroRecordBuilder.setValue(fieldName: String, value: T, serializer: AvroSerializer<T>) = setValueOrNull(fieldName, value, serializer)

/** Sets a field by serializing [value] if non-null, or unsetting it if null. */
fun <T : Any> AvroRecordBuilder.setValueOrNull(fieldName: String, value: T?, serializer: AvroSerializer<T>) = set(fieldName, value?.let { serializer.serialize(it) })

/** Sets a list field by serializing each value to a nested [GenericRecord]. */
fun <T : Any> AvroRecordBuilder.setValues(fieldName: String, values: List<T>, serializer: AvroSerializer<T>) = setValuesOrNull(fieldName, values, serializer)
/** Sets a list field if non-null, serializing each value to a nested [GenericRecord]. */
fun <T : Any> AvroRecordBuilder.setValuesOrNull(fieldName: String, values: List<T>?, serializer: AvroSerializer<T>) = values?.map { serializer.serialize(it) }?.let { setRecords(fieldName, it) }