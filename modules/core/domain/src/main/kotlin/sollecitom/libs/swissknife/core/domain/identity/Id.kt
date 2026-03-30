package sollecitom.libs.swissknife.core.domain.identity

import sollecitom.libs.swissknife.core.domain.traits.DataSerializable
import sollecitom.libs.swissknife.core.domain.traits.StringSerializable

/** Base type for all identifier representations. Supports string and byte-array serialization. */
sealed interface Id : StringSerializable, DataSerializable {

    override val bytesValue: ByteArray get() = stringValue.toByteArray()

    companion object
}

