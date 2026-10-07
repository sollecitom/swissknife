package sollecitom.libs.swissknife.security.scan

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
interface ImageSecurityScanTestSpecification {

    @Test
    fun `the Docker image has no unacceptable vulnerabilities`() {

        assertThatImage(imageUnderScan).hasNoUnacceptableVulnerabilities()
    }

    private val imageUnderScan: String get() = System.getProperty(IMAGE_NAME_PROPERTY) ?: error("$IMAGE_NAME_PROPERTY system property is required")

    private companion object {
        const val IMAGE_NAME_PROPERTY = "securityScan.imageName"
    }
}
