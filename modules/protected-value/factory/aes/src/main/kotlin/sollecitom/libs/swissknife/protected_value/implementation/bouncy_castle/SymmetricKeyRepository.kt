package sollecitom.libs.swissknife.protected_value.implementation.bouncy_castle

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.cryptography.domain.symmetric.SymmetricKey
import sollecitom.libs.swissknife.protected_value.domain.ProtectedValue

interface SymmetricKeyRepository {

    suspend fun lookupKeyForOwner(owner: Id): SymmetricKey?

    suspend fun lookupKeyForProtectedValue(value: ProtectedValue<*, *>): SymmetricKey?
}