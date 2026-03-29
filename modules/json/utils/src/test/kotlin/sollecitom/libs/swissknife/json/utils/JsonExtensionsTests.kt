package sollecitom.libs.swissknife.json.utils

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import kotlin.time.Clock
import java.math.BigDecimal
import java.math.BigInteger

@TestInstance(PER_CLASS)
class JsonExtensionsTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class LongExtensions {

        @Test
        fun `getting a long value that exists`() {

            val json = JSONObject().put("count", 42L)

            val result = json.getLongOrNull("count")

            assertThat(result).isEqualTo(42L)
        }

        @Test
        fun `getting a long value that does not exist returns null`() {

            val json = JSONObject()

            val result = json.getLongOrNull("missing")

            assertThat(result).isNull()
        }

        @Test
        fun `getting a required long value`() {

            val json = JSONObject().put("count", 99L)

            val result = json.getRequiredLong("count")

            assertThat(result).isEqualTo(99L)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class StringExtensions {

        @Test
        fun `getting a string value that exists`() {

            val json = JSONObject().put("name", "test")

            val result = json.getStringOrNull("name")

            assertThat(result).isEqualTo("test")
        }

        @Test
        fun `getting a string value that does not exist returns null`() {

            val json = JSONObject()

            val result = json.getStringOrNull("missing")

            assertThat(result).isNull()
        }

        @Test
        fun `getting a required string value`() {

            val json = JSONObject().put("name", "value")

            val result = json.getRequiredString("name")

            assertThat(result).isEqualTo("value")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class IntExtensions {

        @Test
        fun `getting an int value that exists`() {

            val json = JSONObject().put("age", 25)

            val result = json.getIntOrNull("age")

            assertThat(result).isEqualTo(25)
        }

        @Test
        fun `getting an int value that does not exist returns null`() {

            val json = JSONObject()

            val result = json.getIntOrNull("missing")

            assertThat(result).isNull()
        }

        @Test
        fun `getting a required int value`() {

            val json = JSONObject().put("age", 30)

            val result = json.getRequiredInt("age")

            assertThat(result).isEqualTo(30)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class DoubleExtensions {

        @Test
        fun `getting a double value that exists`() {

            val json = JSONObject().put("rate", 3.14)

            val result = json.getDoubleOrNull("rate")

            assertThat(result).isEqualTo(3.14)
        }

        @Test
        fun `getting a double value that does not exist returns null`() {

            val json = JSONObject()

            val result = json.getDoubleOrNull("missing")

            assertThat(result).isNull()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class BooleanExtensions {

        @Test
        fun `getting a boolean value that exists`() {

            val json = JSONObject().put("active", true)

            val result = json.getBooleanOrNull("active")

            assertThat(result).isEqualTo(true)
        }

        @Test
        fun `getting a boolean value that does not exist returns null`() {

            val json = JSONObject()

            val result = json.getBooleanOrNull("missing")

            assertThat(result).isNull()
        }

        @Test
        fun `getting a required boolean value`() {

            val json = JSONObject().put("active", false)

            val result = json.getRequiredBoolean("active")

            assertThat(result).isEqualTo(false)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class BigIntegerExtensions {

        @Test
        fun `getting a big integer value that exists`() {

            val expected = BigInteger("12345678901234567890")
            val json = JSONObject().put("big", expected)

            val result = json.getBigIntegerOrNull("big")

            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `getting a big integer value that does not exist returns null`() {

            val json = JSONObject()

            val result = json.getBigIntegerOrNull("missing")

            assertThat(result).isNull()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class BigDecimalExtensions {

        @Test
        fun `getting a big decimal value that exists`() {

            val expected = BigDecimal("123.456")
            val json = JSONObject().put("decimal", expected)

            val result = json.getBigDecimalOrNull("decimal")

            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `getting a big decimal value that does not exist returns null`() {

            val json = JSONObject()

            val result = json.getBigDecimalOrNull("missing")

            assertThat(result).isNull()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class JSONObjectExtensions {

        @Test
        fun `getting a nested JSON object that exists`() {

            val nested = JSONObject().put("key", "value")
            val json = JSONObject().put("nested", nested)

            val result = json.getJSONObjectOrNull("nested")

            assertThat(result).isNotNull()
            assertThat(result!!.getString("key")).isEqualTo("value")
        }

        @Test
        fun `getting a nested JSON object that does not exist returns null`() {

            val json = JSONObject()

            val result = json.getJSONObjectOrNull("missing")

            assertThat(result).isNull()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class ArrayExtensions {

        @Test
        fun `getting an array that exists`() {

            val array = JSONArray().put("a").put("b")
            val json = JSONObject().put("items", array)

            val result = json.getArrayOrNull("items")

            assertThat(result).isNotNull()
            assertThat(result!!.length()).isEqualTo(2)
        }

        @Test
        fun `getting an array that does not exist returns null`() {

            val json = JSONObject()

            val result = json.getArrayOrNull("missing")

            assertThat(result).isNull()
        }

        @Test
        fun `getting a string array`() {

            val json = JSONObject().put("tags", JSONArray().put("a").put("b").put("c"))

            val result = json.getRequiredStringArray("tags")

            assertThat(result).isEqualTo(listOf("a", "b", "c"))
        }

        @Test
        fun `getting an int array`() {

            val json = JSONObject().put("numbers", JSONArray().put(1).put(2).put(3))

            val result = json.getRequiredIntArray("numbers")

            assertThat(result).isEqualTo(listOf(1, 2, 3))
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class DeepEquality {

        @Test
        fun `deep equals returns true for equal JSON objects`() {

            val json1 = JSONObject().put("a", 1).put("b", "test")
            val json2 = JSONObject().put("a", 1).put("b", "test")

            val result = json1.deepEquals(json2)

            assertThat(result).isTrue()
        }

        @Test
        fun `deep equals returns false for different JSON objects`() {

            val json1 = JSONObject().put("a", 1)
            val json2 = JSONObject().put("a", 2)

            val result = json1.deepEquals(json2)

            assertThat(result).isFalse()
        }

        @Test
        fun `deep equals for JSON arrays`() {

            val array1 = JSONArray().put(1).put(2)
            val array2 = JSONArray().put(1).put(2)

            val result = array1.deepEquals(array2)

            assertThat(result).isTrue()
        }

        @Test
        fun `deep hash code is consistent for equal objects`() {

            val json1 = JSONObject().put("a", 1)
            val json2 = JSONObject().put("a", 1)

            assertThat(json1.deepHashCode()).isEqualTo(json2.deepHashCode())
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class InstantExtensions {

        @Test
        fun `putting and getting an instant`() {

            val instant = Clock.System.now()
            val json = JSONObject().putInstant("time", instant)

            val result = json.getInstantOrNull("time")

            assertThat(result).isEqualTo(instant)
        }

        @Test
        fun `getting an instant that does not exist returns null`() {

            val json = JSONObject()

            val result = json.getInstantOrNull("missing")

            assertThat(result).isNull()
        }

        @Test
        fun `getting a required instant`() {

            val instant = Clock.System.now()
            val json = JSONObject().putInstant("time", instant)

            val result = json.getRequiredInstant("time")

            assertThat(result).isEqualTo(instant)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class ByteEncodingExtensions {

        @Test
        fun `putting and getting bytes as base64`() {

            val bytes = "hello".toByteArray()
            val json = JSONObject().putBytesAsBase64String("data", bytes)

            val result = json.getBytesFromBase64String("data")

            assertThat(result.toList()).isEqualTo(bytes.toList())
        }

        @Test
        fun `getting base64 bytes that do not exist returns null`() {

            val json = JSONObject()

            val result = json.getBytesFromBase64StringOrNull("missing")

            assertThat(result).isNull()
        }

        @Test
        fun `putting and getting bytes as hex string`() {

            val bytes = byteArrayOf(0x0A, 0x1B, 0x2C)
            val json = JSONObject().putBytesAsHexString("data", bytes)

            val result = json.getBytesFromHexString("data")

            assertThat(result.toList()).isEqualTo(bytes.toList())
        }

        @Test
        fun `getting hex bytes that do not exist returns null`() {

            val json = JSONObject()

            val result = json.getBytesFromHexStringOrNull("missing")

            assertThat(result).isNull()
        }
    }
}
