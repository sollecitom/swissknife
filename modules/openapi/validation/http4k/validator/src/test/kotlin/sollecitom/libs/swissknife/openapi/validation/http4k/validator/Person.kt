package sollecitom.libs.swissknife.openapi.validation.http4k.validator

import sollecitom.libs.swissknife.core.domain.text.Name
import org.json.JSONObject

data class Person(val firstName: Name, val lastName: Name, val age: Int) {

    init {
        require(firstName != lastName) { "First name cannot be equal to last name" } // just an example to showcase domain constraints handling
        require(age > 0)
    }
}

fun Person.toJson() = JSONObject().apply {

    put("firstName", firstName.value)
    put("lastName", lastName.value)
    put("age", age)
}