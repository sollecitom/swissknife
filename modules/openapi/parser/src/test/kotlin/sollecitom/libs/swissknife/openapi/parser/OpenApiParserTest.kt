package sollecitom.libs.swissknife.openapi.parser

import assertk.assertThat
import assertk.assertions.isSuccess
import sollecitom.libs.swissknife.test.utils.assertions.failedThrowing
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.util.jar.JarEntry
import java.util.jar.JarOutputStream

@TestInstance(PER_CLASS)
private class OpenApiParserTest {

    @Test
    fun `parser is able to read and construct OpenAPI object`() {

        val parser = newOpenApiParser()
        val validOpenApiLocation = "api/ValidSwagger.yml"

        val result = runCatching { parser.parse(validOpenApiLocation) }

        assertThat(result).isSuccess()
    }

    @Test
    fun `parser is able to read and construct OpenAPI object via its URL`() {

        val parser = newOpenApiParser()
        val validOpenApiLocation = readResourceWithName("api/ValidSwagger.yml")

        val result = runCatching { parser.parse(validOpenApiLocation) }

        assertThat(result).isSuccess()
    }

    @Test
    fun `parser is able to read and construct OpenAPI object via a URL inside a jar`() {

        val parser = newOpenApiParser()
        val jar = jarWithEntry(name = "api/api.yml", content = selfContainedOpenApi)
        val validOpenApiLocation = URI("jar:${jar.toUri()}!/api/api.yml").toURL()

        val result = runCatching { parser.parse(validOpenApiLocation) }

        assertThat(result).isSuccess()
    }

    @Test
    fun `parser is unable to read and construct OpenAPI invalid object`() {

        val parser = newOpenApiParser()
        val invalidOpenApiLocation = "api/InvalidSwagger.yml"

        val result = runCatching { parser.parse(invalidOpenApiLocation) }

        assertThat(result).failedThrowing<OpenApiParser.ParseException>()
    }

    private fun newOpenApiParser(): OpenApiParser = OpenApiReader

    private fun jarWithEntry(name: String, content: String): Path {

        val jar = Files.createTempFile("openapi", ".jar").apply { toFile().deleteOnExit() }
        JarOutputStream(Files.newOutputStream(jar)).use { output ->
            output.putNextEntry(JarEntry(name))
            output.write(content.toByteArray())
            output.closeEntry()
        }
        return jar
    }

    private val selfContainedOpenApi = """
        openapi: 3.1.0
        info:
          version: 1.0.0
          title: Sample API
        paths:
          /list:
            get:
              responses:
                '200':
                  description: Successful response
    """.trimIndent()
}