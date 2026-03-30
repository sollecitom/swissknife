package sollecitom.libs.swissknife.core.utils

import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdFactory

/** Provides access to a [UniqueIdFactory] for generating unique identifiers. */
interface UniqueIdGenerator {

    val newId: UniqueIdFactory
}