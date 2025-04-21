package sollecitom.libs.swissknife.correlation.core.domain.access.origin

import sollecitom.libs.swissknife.core.domain.networking.IpAddress
import sollecitom.libs.swissknife.web.client.info.domain.ClientInfo

data class Origin(val ipAddress: IpAddress?, val clientInfo: ClientInfo) {

    companion object
}