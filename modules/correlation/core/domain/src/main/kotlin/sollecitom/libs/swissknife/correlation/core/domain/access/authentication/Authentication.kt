package sollecitom.libs.swissknife.correlation.core.domain.access.authentication

import sollecitom.libs.swissknife.core.domain.identity.Id
import kotlinx.datetime.Instant

sealed interface Authentication {

    data class Token(val id: Id, val validFrom: Instant?, val validTo: Instant?) {

        companion object
    }

    companion object
}