package sollecitom.libs.swissknife.core.domain.identity.factory

import sollecitom.libs.swissknife.core.domain.identity.Id

interface UniqueIdentifierFactory<out ID : Id> {

    operator fun invoke(): ID

    operator fun invoke(value: String): ID
}