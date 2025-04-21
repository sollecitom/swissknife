package sollecitom.libs.swissknife.web.client.info.analyzer

import sollecitom.libs.swissknife.web.client.info.domain.ClientInfo

interface ClientInfoAnalyzer {

    operator fun invoke(userAgentValue: String): ClientInfo

    companion object
}
