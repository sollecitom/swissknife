package sollecitom.libs.swissknife.web.api.test.utils

import sollecitom.libs.swissknife.core.domain.networking.Port
import sollecitom.libs.swissknife.core.domain.networking.http.HttpProtocol
import sollecitom.libs.swissknife.web.service.domain.WithWebInterface

fun WithWebInterface.httpURLWithPath(path: String, port: Port = webInterface.port, protocol: HttpProtocol = HttpProtocol.HTTP) = "${protocol.value}://${webInterface.host}:${port.value}/$path"