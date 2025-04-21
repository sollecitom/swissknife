package sollecitom.libs.swissknife.protected_value.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.text.Name

interface ProtectedValue<out VALUE : Any, out METADATA> {

    val value: ByteArray
    val name: Name
    val owner: Id
    val metadata: METADATA

    interface Accessible<out VALUE : Any, out METADATA, in ACCESS_CONTEXT : Any> : ProtectedValue<VALUE, METADATA> {

        suspend fun access(context: ACCESS_CONTEXT): VALUE
    }

    companion object
}