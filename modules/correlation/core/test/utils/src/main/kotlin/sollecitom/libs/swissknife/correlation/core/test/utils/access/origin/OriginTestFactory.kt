package sollecitom.libs.swissknife.correlation.core.test.utils.access.origin

import sollecitom.libs.swissknife.core.domain.networking.IpAddress
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import sollecitom.libs.swissknife.web.client.info.domain.ClientInfo

fun Origin.Companion.create(ipAddress: IpAddress = IpAddress.V4.localhost, clientInfo: ClientInfo = ClientInfo.create()): Origin = Origin(ipAddress, clientInfo)