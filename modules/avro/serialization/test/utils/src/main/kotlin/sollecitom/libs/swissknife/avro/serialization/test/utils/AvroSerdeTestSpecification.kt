package sollecitom.libs.swissknife.avro.serialization.test.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerde
import sollecitom.libs.swissknife.avro.serialization.utils.AvroSerializationUtils.writeAsBytes
import sollecitom.libs.swissknife.avro.serialization.utils.deserializeFromBytes
import sollecitom.libs.swissknife.test.utils.params.TestArgument
import sollecitom.libs.swissknife.test.utils.params.parameterizedTestArguments

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface AvroSerdeTestSpecification<VALUE : Any> {

    val avroSerde: AvroSerde<VALUE>

    fun parameterizedArguments(): List<Pair<String, VALUE>>

    fun arguments() = parameterizedTestArguments(*parameterizedArguments().toTypedArray())

    @ParameterizedTest
    @MethodSource("arguments")
    fun `serializing and deserializing to and from Avro`(argument: TestArgument<VALUE>) {

        val value = argument.value

        val record = avroSerde.serialize(value)
        val bytes = writeAsBytes(record)
        val deserialized = avroSerde.deserializeFromBytes(bytes)

        assertThat(deserialized).isEqualTo(value)
    }
}