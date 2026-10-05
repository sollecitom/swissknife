package sollecitom.libs.swissknife.json.utils

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Nested
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

    @Nested
    @TestInstance(PER_CLASS)
    inner class Validation {

        @Test
        fun `an object is validated against its schema`() {

            val schema = JSONObject("""{"type":"object","required":["id"]}""").asSchema()

            assertThat(schema.validate(JSONObject("""{"id":1}"""))).isNull()
            assertThat(schema.validate(JSONObject("""{"name":"a"}"""))).isNotNull()
        }

        @Test
        fun `an array is validated against its schema`() {

            val schema = JSONObject("""{"type":"array","items":{"type":"integer"}}""").asSchema()

            assertThat(schema.validate(JSONArray("""[1, 2]"""))).isNull()
            assertThat(schema.validate(JSONArray("""["a"]"""))).isNotNull()
        }

        @Test
        fun `a string is validated against its schema`() {

            val schema = JSONObject("""{"type":"string","maxLength":3}""").asSchema()

            assertThat(schema.validate("abc")).isNull()
            assertThat(schema.validate("abcd")).isNotNull()
            assertThat(schema.validate(1)).isNotNull()
        }

        @Test
        fun `a number is validated against its schema`() {

            val schema = JSONObject("""{"type":"integer","minimum":1}""").asSchema()

            assertThat(schema.validate(1)).isNull()
            assertThat(schema.validate(0)).isNotNull()
            assertThat(schema.validate(1.5)).isNotNull()
        }

        @Test
        fun `a boolean is validated against its schema`() {

            val schema = JSONObject("""{"type":"boolean"}""").asSchema()

            assertThat(schema.validate(true)).isNull()
            assertThat(schema.validate("true")).isNotNull()
        }
    }
}
