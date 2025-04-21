package sollecitom.libs.swissknife.core.domain.identity.factory.string

import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdentifierFactory
import kotlin.random.Random

internal class StringFactoryAdapter(private val random: Random, private val string: Random.() -> String) : UniqueIdentifierFactory<StringId> {

    override fun invoke() = invoke(random.string())

    override fun invoke(value: String) = StringId(value)
}