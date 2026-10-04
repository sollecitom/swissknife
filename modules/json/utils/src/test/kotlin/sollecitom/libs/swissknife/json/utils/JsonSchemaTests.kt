package sollecitom.libs.swissknife.json.utils

import assertk.assertThat
import assertk.assertions.isEmpty
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class JsonSchemaTests {

    @Test
    fun `a boolean property schema has no properties`() {

        val schema = JSONObject("""{"type":"object","properties":{"anything":true}}""").asSchema()

        val anythingSchema = schema.properties.single().schema

        assertThat(anythingSchema.properties).isEmpty()
    }
}
