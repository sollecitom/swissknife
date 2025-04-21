package sollecitom.libs.swissknife.core.domain.identity

import sollecitom.libs.swissknife.core.domain.text.Name

data class InstanceInfo(val nodeName: Name, val groupName: Name) {

    init {
        require(nodeName != groupName) { "Instance node name cannot be the same as the instance group name" }
    }
}