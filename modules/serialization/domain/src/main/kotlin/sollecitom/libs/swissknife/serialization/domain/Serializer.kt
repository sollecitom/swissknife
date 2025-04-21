package sollecitom.libs.swissknife.serialization.domain

interface Serializer<in VALUE : Any, out SERIALIZED : Any> {

    fun serialize(value: VALUE): SERIALIZED
}

