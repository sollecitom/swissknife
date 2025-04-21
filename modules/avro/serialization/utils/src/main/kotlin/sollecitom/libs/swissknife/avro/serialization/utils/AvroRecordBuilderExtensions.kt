package sollecitom.libs.swissknife.avro.serialization.utils

fun <T : Any> AvroRecordBuilder.setValue(fieldName: String, value: T, serializer: AvroSerializer<T>) = setValueOrNull(fieldName, value, serializer)

fun <T : Any> AvroRecordBuilder.setValueOrNull(fieldName: String, value: T?, serializer: AvroSerializer<T>) = set(fieldName, value?.let { serializer.serialize(it) })

fun <T : Any> AvroRecordBuilder.setValues(fieldName: String, values: List<T>, serializer: AvroSerializer<T>) = setValuesOrNull(fieldName, values, serializer)
fun <T : Any> AvroRecordBuilder.setValuesOrNull(fieldName: String, values: List<T>?, serializer: AvroSerializer<T>) = values?.map { serializer.serialize(it) }?.let { setRecords(fieldName, it) }