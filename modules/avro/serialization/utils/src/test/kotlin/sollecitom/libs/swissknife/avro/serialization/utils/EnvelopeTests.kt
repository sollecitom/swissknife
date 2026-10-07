package sollecitom.libs.swissknife.avro.serialization.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.apache.avro.Schema
import sollecitom.libs.swissknife.avro.serialization.test.utils.serializeAndDeserializeWith
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class EnvelopeTests {

    private val schema = Schema.Parser().parse(
        """
        {"type":"record","name":"Actor","fields":[
            {"name":"envelope","type":[
                {"type":"record","name":"Direct","fields":[{"name":"user","type":"string"}]},
                {"type":"record","name":"OnBehalf","fields":[{"name":"user","type":"string"},{"name":"onBehalfOf","type":"string"}]}
            ]}
        ]}
        """.trimIndent()
    )

    @Test
    fun `the union branch is identified by the record name`() {

        val record = buildGenericRecord(schema) { setEnvelope("OnBehalf") { set("user", "a"); set("onBehalfOf", "b") } }

        val branchAndValue = record.serializeAndDeserializeWith(schema).getEnvelope { branchName, envelope -> branchName to envelope.getString("onBehalfOf") }

        assertThat(branchAndValue).isEqualTo("OnBehalf" to "b")
    }
}
