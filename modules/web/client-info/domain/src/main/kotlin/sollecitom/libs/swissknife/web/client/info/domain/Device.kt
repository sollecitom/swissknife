package sollecitom.libs.swissknife.web.client.info.domain

import sollecitom.libs.swissknife.core.domain.text.Name

data class Device(val className: Name?, val name: Name?, val brand: Name?) {

    companion object {
        val unknown = Device(className = null, name = null, brand = null)
    }
}