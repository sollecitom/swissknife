package sollecitom.libs.swissknife.protected_value.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.text.Name

class TypedProtectedValueFactory<VALUE : Any, out METADATA, in ACCESS_CONTEXT : Any>(private val factory: ProtectedValueFactory<ACCESS_CONTEXT, METADATA>, private val serialize: (VALUE) -> ByteArray) : ProtectedValueFactory.Typed<VALUE, METADATA, ACCESS_CONTEXT> {

    override suspend fun protectValue(value: VALUE, valueName: Name, owner: Id) = factory.protectValue(value, valueName, owner, serialize)

    class Accessible<VALUE : Any, METADATA, in ACCESS_CONTEXT : Any>(private val factory: ProtectedValueFactory.Accessible<ACCESS_CONTEXT, METADATA>, private val serialize: (VALUE) -> ByteArray, private val deserialize: (ByteArray) -> VALUE) : ProtectedValueFactory.Typed.Accessible<VALUE, METADATA, ACCESS_CONTEXT> {

        override suspend fun protectValue(value: VALUE, valueName: Name, owner: Id): ProtectedValue<VALUE, METADATA> = factory.protectValue(value, valueName, owner, serialize)

        override fun makeAccessible(protectedValue: ProtectedValue<VALUE, METADATA>) = factory.makeAccessible(protectedValue, deserialize)
    }
}

internal suspend fun <VALUE : Any, METADATA, ACCESS_CONTEXT : Any> ProtectedValueFactory.Typed.Accessible<VALUE, METADATA, ACCESS_CONTEXT>.protectAndMakeAccessible(value: VALUE, name: Name, owner: Id): ProtectedValue.Accessible<VALUE, METADATA, ACCESS_CONTEXT> = makeAccessible(protectValue(value, name, owner))

fun <VALUE : Any, METADATA, ACCESS_CONTEXT : Any> ProtectedValueFactory<ACCESS_CONTEXT, METADATA>.forType(serialize: (VALUE) -> ByteArray): ProtectedValueFactory.Typed<VALUE, METADATA, ACCESS_CONTEXT> = TypedProtectedValueFactory(this, serialize)

fun <VALUE : Any, METADATA, ACCESS_CONTEXT : Any> ProtectedValueFactory.Accessible<ACCESS_CONTEXT, METADATA>.forType(serialize: (VALUE) -> ByteArray, deserialize: (ByteArray) -> VALUE): ProtectedValueFactory.Typed.Accessible<VALUE, METADATA, ACCESS_CONTEXT> = TypedProtectedValueFactory.Accessible(this, serialize, deserialize)