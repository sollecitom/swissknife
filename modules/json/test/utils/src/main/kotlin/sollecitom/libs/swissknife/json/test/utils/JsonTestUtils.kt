package sollecitom.libs.swissknife.json.test.utils

import assertk.Assert
import assertk.assertions.containsAtLeast
import assertk.assertions.containsOnly
import assertk.assertions.support.expected
import assertk.assertions.support.show
import sollecitom.libs.swissknife.json.utils.JsonSchema
import sollecitom.libs.swissknife.test.utils.assertions.containsSameEntriesAs
import org.json.JSONObject

fun Assert<JSONObject>.containsOnly(pair: Pair<String, Any>, vararg others: Pair<String, Any>) = given { actual ->

    assertThat(actual.toMap()).containsOnly(*arrayOf(pair) + others)
}

fun Assert<JSONObject>.containsAtLeast(pair: Pair<String, Any>, vararg others: Pair<String, Any>) = given { actual ->

    assertThat(actual.toMap()).containsAtLeast(*arrayOf(pair) + others)
}

fun Assert<JSONObject>.containsSameEntriesAs(expected: JSONObject) = given { actual ->

    assertThat(actual.toMap()).containsSameEntriesAs(expected.toMap())
}

fun Assert<JSONObject>.compliesWith(schema: JsonSchema) = given { actual ->

    val failure = schema.validate(actual)
    if (failure != null) {
        expected("JSON that complies with schema at :${show(schema.location)} but there were errors: ${show(failure.message)}")
    }
}

fun Assert<JSONObject>.doesNotComplyWith(schema: JsonSchema) = given { actual ->

    schema.validate(actual) ?: expected("JSON that does not comply with schema at :${show(schema.location)} but there were no errors")
}