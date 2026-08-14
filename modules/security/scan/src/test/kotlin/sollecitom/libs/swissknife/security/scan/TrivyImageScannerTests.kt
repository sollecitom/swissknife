package sollecitom.libs.swissknife.security.scan

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.junit.jupiter.api.assertThrows
import java.io.File

@TestInstance(PER_CLASS)
class TrivyImageScannerTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class Retrying {

        @Test
        fun `a truncated database download is retried`() {

            // The failure that makes the scan flaky: Trivy aborts part-way through fetching the Java DB.
            val output = """
                2026-08-14T08:54:28Z	INFO	[javadb] Downloading artifact...	repo="mirror.gcr.io/aquasec/trivy-java-db:1"
                2026-08-14T08:55:07Z	FATAL	Fatal error	run error: Unable to initialize the Java DB: Java DB update failed: OCI artifact error: failed to download Java DB: oci download error: copy error: unexpected EOF
            """.trimIndent()

            assertThat(TrivyImageScanner.isWorthRetrying(output)).isTrue()
        }

        @Test
        fun `a missing image is not retried`() {

            // Not transient: retrying only multiplies the wait before reporting the same real error.
            val output = """
                2026-08-14T08:51:07Z	FATAL	Fatal error	run error: image scan error: unable to find the specified image "ghcr.io/example/service:latest" in ["docker" "containerd" "podman" "remote"]
            """.trimIndent()

            assertThat(TrivyImageScanner.isWorthRetrying(output)).isFalse()
        }

        @Test
        fun `an empty output is retried`() {

            assertThat(TrivyImageScanner.isWorthRetrying("")).isTrue()
        }

        @Test
        fun `at least one attempt is required`() {

            assertThrows<IllegalArgumentException> { TrivyImageScanner.scan(imageName = "example:latest", maximumAttempts = 0) }
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class CacheDirectory {

        @Test
        fun `defaults to the conventional Trivy cache location`() {

            val previous = System.clearProperty("securityScan.cacheDirectory")
            try {
                val expected = File(System.getProperty("user.home"), ".cache/trivy")

                assertThat(TrivyImageScanner.defaultCacheDirectory()).isEqualTo(expected)
            } finally {
                previous?.let { System.setProperty("securityScan.cacheDirectory", it) }
            }
        }

        @Test
        fun `can be overridden with a system property`() {

            val previous = System.getProperty("securityScan.cacheDirectory")
            System.setProperty("securityScan.cacheDirectory", "/tmp/a-custom-trivy-cache")
            try {
                assertThat(TrivyImageScanner.defaultCacheDirectory()).isEqualTo(File("/tmp/a-custom-trivy-cache"))
            } finally {
                if (previous == null) System.clearProperty("securityScan.cacheDirectory") else System.setProperty("securityScan.cacheDirectory", previous)
            }
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class ParsingTrivyOutput {

        @Test
        fun `extracts vulnerabilities from a report`() {

            val vulnerabilities = TrivyImageScanner.parseVulnerabilities(reportWithOneVulnerability)

            assertThat(vulnerabilities).hasSize(1)
            val vulnerability = vulnerabilities.single()
            assertThat(vulnerability::id).isEqualTo("CVE-2026-12345")
            assertThat(vulnerability::packageName).isEqualTo("openssl")
            assertThat(vulnerability::installedVersion).isEqualTo("3.0.1")
            assertThat(vulnerability::fixedVersion).isEqualTo("3.0.2")
            assertThat(vulnerability::severity).isEqualTo(Severity.CRITICAL)
            assertThat(vulnerability::target).isEqualTo("ubuntu (24.04)")
            assertThat(vulnerability::targetClass).isEqualTo("os-pkgs")
        }

        @Test
        fun `ignores the log lines Trivy interleaves with the JSON`() {

            // Testcontainers merges stdout and stderr, so the JSON arrives surrounded by log output.
            val output = "INFO\t[vulndb] Downloading vulnerability DB...\n$reportWithOneVulnerability\nINFO\tdone"

            assertThat(TrivyImageScanner.parseVulnerabilities(output)).hasSize(1)
        }

        @Test
        fun `returns nothing when a result has no vulnerabilities`() {

            val output = """{"Results":[{"Target":"ubuntu (24.04)","Class":"os-pkgs"}]}"""

            assertThat(TrivyImageScanner.parseVulnerabilities(output)).isEmpty()
        }

        @Test
        fun `returns nothing when there is no JSON at all`() {

            assertThat(TrivyImageScanner.parseVulnerabilities("FATAL something went wrong")).isEmpty()
        }

        @Test
        fun `an unreadable report fails instead of reporting the image as clean`() {

            // Regression: truncated output used to parse as zero vulnerabilities, silently passing the security
            // gate for an image that was never actually scanned.
            assertThrows<TrivyScanFailed> { TrivyImageScanner.parseReport("FATAL something went wrong", "example:latest", "aquasec/trivy:0.73.0") }
        }

        @Test
        fun `a truncated report fails rather than under-reporting`() {

            val truncated = reportWithOneVulnerability.substring(0, reportWithOneVulnerability.length / 2)

            assertThrows<TrivyScanFailed> { TrivyImageScanner.parseReport(truncated, "example:latest", "aquasec/trivy:0.73.0") }
        }

        @Test
        fun `a genuinely clean report is not treated as a failure`() {

            // Trivy omits "Results" when there is nothing to report, which must stay distinguishable from
            // output it could not read at all.
            val clean = """{"SchemaVersion":2,"ArtifactName":"example:latest","ArtifactType":"container_image"}"""

            assertThat(TrivyImageScanner.parseReport(clean, "example:latest", "aquasec/trivy:0.73.0")).isEmpty()
        }

        @Test
        fun `a report listing no vulnerabilities is not treated as a failure`() {

            val clean = """{"SchemaVersion":2,"Results":[{"Target":"ubuntu (24.04)","Class":"os-pkgs"}]}"""

            assertThat(TrivyImageScanner.parseReport(clean, "example:latest", "aquasec/trivy:0.73.0")).isEmpty()
        }

        @Test
        fun `an unknown severity does not fail the parse`() {

            val output = reportWithOneVulnerability.replace("\"CRITICAL\"", "\"NOT_A_SEVERITY\"")

            assertThat(TrivyImageScanner.parseVulnerabilities(output).single()::severity).isEqualTo(Severity.UNKNOWN)
        }

        @Test
        fun `a missing fixed version is null`() {

            val output = reportWithOneVulnerability.replace("\"FixedVersion\": \"3.0.2\",", "")

            assertThat(TrivyImageScanner.parseVulnerabilities(output).single()::fixedVersion).isNull()
        }

        private val reportWithOneVulnerability = """
            {
              "Results": [
                {
                  "Target": "ubuntu (24.04)",
                  "Class": "os-pkgs",
                  "Vulnerabilities": [
                    {
                      "VulnerabilityID": "CVE-2026-12345",
                      "PkgName": "openssl",
                      "InstalledVersion": "3.0.1",
                      "FixedVersion": "3.0.2",
                      "Severity": "CRITICAL",
                      "Title": "a serious problem"
                    }
                  ]
                }
              ]
            }
        """.trimIndent()
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Failures {

        @Test
        fun `the error explains what failed and includes the Trivy output`() {

            val failure = TrivyScanFailed(imageName = "example:latest", trivyImage = "aquasec/trivy:0.73.0", attempts = 3, output = "FATAL unexpected EOF", cause = null)

            assertThat(failure.message!!).contains("example:latest")
            assertThat(failure.message!!).contains("aquasec/trivy:0.73.0")
            assertThat(failure.message!!).contains("3 attempt(s)")
            assertThat(failure.message!!).contains("FATAL unexpected EOF")
        }

        @Test
        fun `the error is explicit when no output was captured`() {

            val failure = TrivyScanFailed(imageName = "example:latest", trivyImage = "aquasec/trivy:0.73.0", attempts = 1, output = "", cause = null)

            assertThat(failure.message!!).contains("<no output captured>")
        }
    }
}
