package sollecitom.libs.swissknife.correlation.core.domain.access.authentication

import sollecitom.libs.swissknife.core.domain.identity.Id
import kotlin.time.Instant

/** Represents the authentication method used by an actor (e.g. credentials, token, federated). */
sealed interface Authentication {

    data class Token(val id: Id, val validFrom: Instant?, val validTo: Instant?) {

        companion object
    }

    companion object
}