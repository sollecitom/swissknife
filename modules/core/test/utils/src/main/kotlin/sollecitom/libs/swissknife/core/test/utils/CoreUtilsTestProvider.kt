package sollecitom.libs.swissknife.core.test.utils

import sollecitom.libs.swissknife.core.domain.identity.InstanceInfo
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.core.utils.provider

val CoreDataGenerator.Companion.testProvider: CoreDataGenerator by lazy { CoreDataGenerator.provider() }

private val testInstanceInfo = InstanceInfo(nodeName = "test-instance-node-name".let(::Name), groupName = "test-instance-group-name".let(::Name))