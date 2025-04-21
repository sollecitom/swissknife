package sollecitom.libs.swissknife.web.client.info.analyzer

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ParsingTheOriginExampleTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    private val analyzer = ClientInfoAnalyzer.instance

    @Test
    fun `from the user agent added by an HTTP client`() {

        val rawUserAgent = "Apache-HttpAsyncClient/5.2.1 (Java/19.0.2)"

        val clientInfo = analyzer(rawUserAgent)

        assertThat(clientInfo.device.className?.value).isNotNull().isEqualTo("Mobile")
        assertThat(clientInfo.device.name?.value).isNotNull().isEqualTo("Apache HttpAsyncClient")
        assertThat(clientInfo.device.brand?.value).isNotNull().isEqualTo("Apache")
        assertThat(clientInfo.operatingSystem.className).isNull()
        assertThat(clientInfo.operatingSystem.name).isNull()
        assertThat(clientInfo.operatingSystem.version).isNull()
        assertThat(clientInfo.layoutEngine.className).isNull()
        assertThat(clientInfo.layoutEngine.name).isNull()
        assertThat(clientInfo.layoutEngine.version).isNull()
        assertThat(clientInfo.agent.className?.value).isNotNull().isEqualTo("Special")
        assertThat(clientInfo.agent.name?.value).isNotNull().isEqualTo("Apache-HttpAsyncClient")
        assertThat(clientInfo.agent.version?.value).isNotNull().isEqualTo("5.2.1")
    }
}
