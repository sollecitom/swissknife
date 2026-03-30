package sollecitom.libs.swissknife.service.domain

import sollecitom.libs.swissknife.core.domain.identity.Id
import sollecitom.libs.swissknife.core.domain.identity.InstanceInfo
import sollecitom.libs.swissknife.core.domain.identity.factory.invoke
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.utils.UniqueIdGenerator

/** Identifies a service instance by a unique ID and a service name. */
data class ServiceInfo(val instanceId: Id, val name: Name) {

    /** Creates an [InstanceInfo] for a specific module within this service. */
    fun withModuleName(module: Name): InstanceInfo {

        return InstanceInfo(nodeName = instanceId.stringValue.let(::Name), groupName = "${name.value}$MODULE_NAME_SEPARATOR${module.value}".let(::Name))
    }

    companion object {
        private const val MODULE_NAME_SEPARATOR = "."
    }
}

context(ids: UniqueIdGenerator)
fun ServiceInfo.Companion.withName(name: Name) = ServiceInfo(name = name, instanceId = ids.newId())