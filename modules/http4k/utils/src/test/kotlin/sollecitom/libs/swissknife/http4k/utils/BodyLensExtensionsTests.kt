package sollecitom.libs.swissknife.http4k.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.http4k.core.Body
import org.http4k.core.ContentType
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.lens.LensFailure
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import sollecitom.libs.swissknife.http4k.utils.lens.jsonObject
import sollecitom.libs.swissknife.http4k.utils.lens.map
import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.json.utils.asSchema
import sollecitom.libs.swissknife.json.utils.serde.JsonDeserializer
import sollecitom.libs.swissknife.json.utils.serde.JsonSerde
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing

@TestInstance(PER_CLASS)
class BodyLensExtensionsTests {

    private val lens = Body.jsonObject().map(NameSerde).toLens()

    @Test
    fun `a body matching the serde's schema is deserialized`() {

        val request = """{"name":"Bruce"}""".asJsonRequest()

        val name = lens(request)

        assertThat(name).isEqualTo("Bruce")
    }

    @Test
    fun `a body that doesn't match the serde's schema is a lens failure`() {

        val request = """{"name":""}""".asJsonRequest()

        val result = runCatching { lens(request) }

        assertThat(result).failedThrowing<LensFailure>()
    }

    @Test
    fun `a read-only lens also rejects a body that doesn't match the deserializer's schema`() {

        val readOnlyLens = Body.jsonObject().map(nameDeserializer).toLens()
        val request = """{"name":""}""".asJsonRequest()

        val result = runCatching { readOnlyLens(request) }

        assertThat(result).failedThrowing<LensFailure>()
    }

    private val nameDeserializer: JsonDeserializer<String> = NameSerde

    private fun String.asJsonRequest() = Request(Method.POST, "/names").header("Content-Type", ContentType.APPLICATION_JSON.value).body(this)

    private object NameSerde : JsonSerde.SchemaAware<String> {

        override val schema: JsonSchema = JSONObject("""{"type":"object","required":["name"],"properties":{"name":{"type":"string","minLength":1}}}""").asSchema()

        override fun serialize(value: String) = JSONObject().put("name", value)

        override fun deserialize(value: JSONObject): String = value.getString("name")
    }
}
