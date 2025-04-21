package sollecitom.libs.swissknife.core.utils

import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdFactory

interface UniqueIdGenerator {

    val newId: UniqueIdFactory
}