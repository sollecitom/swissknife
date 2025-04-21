package sollecitom.libs.swissknife.messaging.test.utils.topic

import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.text.random
import sollecitom.libs.swissknife.core.utils.RandomGenerator
import sollecitom.libs.swissknife.messaging.domain.topic.TenantAgnosticTopic
import sollecitom.libs.swissknife.messaging.domain.topic.Topic

context(_: RandomGenerator)
fun Topic.Companion.create(persistent: Boolean = true, tenant: Name = Name.random(), namespaceName: Name = Name.random(), namespace: Topic.Namespace? = Topic.Namespace(tenant = tenant, name = namespaceName), name: Name = Name.random()): Topic = of(persistent, namespace, name)

context(_: RandomGenerator)
fun TenantAgnosticTopic.Companion.create(persistent: Boolean = true, namespaceName: Name = Name.random(), name: Name = Name.random()) = TenantAgnosticTopic(name = name, namespaceName = namespaceName, persistent = persistent)