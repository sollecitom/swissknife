package sollecitom.libs.swissknife.core.domain.identity

import sollecitom.libs.swissknife.core.domain.text.Name

/** Identifies a running instance within a group (e.g. a service node within a cluster). Node and group names must differ. */
data class InstanceInfo(val nodeName: Name, val groupName: Name) {

    init {
        require(nodeName != groupName) { "Instance node name cannot be the same as the instance group name" }
    }
}