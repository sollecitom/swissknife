package sollecitom.libs.swissknife.core.domain.traits

import sollecitom.libs.swissknife.core.domain.identity.Id

/** A type that has a unique [Id]. */
interface Identifiable {

    val id: Id
}