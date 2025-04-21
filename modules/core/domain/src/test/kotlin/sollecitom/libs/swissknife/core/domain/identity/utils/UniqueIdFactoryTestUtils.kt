package sollecitom.libs.swissknife.core.domain.identity.utils

import sollecitom.libs.swissknife.core.domain.identity.InstanceInfo
import sollecitom.libs.swissknife.core.domain.identity.factory.UniqueIdFactory
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.domain.text.Name
import kotlinx.datetime.Clock
import kotlin.random.Random

operator fun UniqueIdFactory.Companion.invoke(random: Random = Random, clock: Clock = Clock.System): UniqueIdFactory = UniqueIdFactory.invoke(random, clock)

private val testInstanceInfo = InstanceInfo(nodeName = "test-instance-node-name".let(::Name), groupName = "test-instance-group-name".let(::Name))