package sollecitom.libs.swissknife.openapi.provider.provider

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.nio.file.Files
import java.nio.file.Path
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

@TestInstance(PER_CLASS)
class LocationBasedOpenApiProviderTests {

    @Test
    fun `reading the raw definition from inside a jar`() {

        val jar = jarWithEntry(name = "api/api.yml", content = "openapi: 3.1.0")
        val provider = LocationBasedOpenApiProvider("jar:${jar.toUri()}!/api/api.yml")

        val rawOpenApi = provider.rawOpenApi

        assertThat(rawOpenApi).isEqualTo("openapi: 3.1.0")
    }

    private fun jarWithEntry(name: String, content: String): Path {

        val jar = Files.createTempFile("openapi", ".jar").apply { toFile().deleteOnExit() }
        JarOutputStream(Files.newOutputStream(jar)).use { output ->
            output.putNextEntry(JarEntry(name))
            output.write(content.toByteArray())
            output.closeEntry()
        }
        return jar
    }
}
