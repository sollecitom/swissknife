package sollecitom.libs.swissknife.core.domain.identity.factory

import sollecitom.libs.swissknife.core.domain.identity.Id

/** Factory for generating unique identifiers of a specific type. */
interface UniqueIdentifierFactory<out ID : Id> {

    /** Generates a new unique identifier. */
    operator fun invoke(): ID

    /** Parses an existing identifier from its string representation. */
    operator fun invoke(value: String): ID
}