package sollecitom.libs.swissknife.json.test.utils

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.opentest4j.AssertionFailedError
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.test.utils.params.TestArgument
import sollecitom.libs.swissknife.test.utils.params.parameterizedTestArguments

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
interface JsonSerdeTestSpecification<VALUE : Any> {

    val jsonSerde: JsonSerde.SchemaAware<VALUE>

    fun parameterizedArguments(): List<Pair<String, VALUE>>

    fun arguments() = parameterizedTestArguments(*parameterizedArguments().toTypedArray())

    fun Assert<VALUE>.matches(expected: VALUE): Unit = isEqualTo(expected)

    @ParameterizedTest
    @MethodSource("arguments")
    fun `serializing and deserializing to and from JSON`(argument: TestArgument<VALUE>) {

        val value = argument.value

        val json = jsonSerde.serialize(value)
        val deserialized = jsonSerde.deserialize(json)

        assertThat(deserialized).matches(value)
        try {
            assertThat(json).compliesWith(jsonSerde.schema)
        } catch (error: AssertionFailedError) {
            println(json)
            throw error
        }
    }
}