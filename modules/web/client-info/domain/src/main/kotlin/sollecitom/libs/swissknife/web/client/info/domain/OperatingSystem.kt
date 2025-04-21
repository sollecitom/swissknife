package sollecitom.libs.swissknife.web.client.info.domain

import sollecitom.libs.swissknife.core.domain.text.Name

data class OperatingSystem(val className: Name?, val name: Name?, val version: Name?) {

    companion object {
        val unknown = OperatingSystem(className = null, name = null, version = null)
    }
}