package sollecitom.libs.swissknife.openapi.provider.definition.reader

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.doesNotContain
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

@TestInstance(PER_CLASS)
class DowngradingOpenApiDefinitionReaderTests {

    @Test
    fun `a quoted 3_1 version is downgraded to 3_0_0`() {

        val location = apiWithRelativeReference(versionLine = "openapi: \"3.1.0\"")

        val definition = OpenApiDefinitionReader.downgrading.read(location.toString())

        assertThat(definition.asYaml).contains("openapi: 3.0.0")
    }

    @Test
    fun `relative references resolve against the original location`() {

        val location = apiWithRelativeReference(versionLine = "openapi: 3.1.0")

        val definition = OpenApiDefinitionReader.downgrading.read(location.toString())

        assertThat(definition.asYaml).contains("thingName")
        assertThat(definition.asYaml).doesNotContain("./schemas/Thing.yaml")
    }

    @Test
    fun `a 3_0_0 definition keeps its version`() {

        val location = apiWithRelativeReference(versionLine = "openapi: 3.0.0")

        val definition = OpenApiDefinitionReader.downgrading.read(location.toString())

        assertThat(definition.asYaml).contains("openapi: 3.0.0")
        assertThat(definition.asYaml).contains("thingName")
    }

    @Test
    fun `a 3_1 JSON definition is downgraded to 3_0_0`() {

        val location = jsonApi(version = "3.1.0")

        val definition = OpenApiDefinitionReader.downgrading.read(location.toString())

        assertThat(definition.asYaml).contains("openapi: 3.0.0")
    }

    private fun jsonApi(version: String): Path {

        val directory = Files.createTempDirectory("openapi-downgrading").apply { toFile().deleteOnExit() }
        return directory.resolve("api.json").apply {
            writeText(
                """
                {"openapi": "$version", "info": {"title": "Things", "version": "1.0.0"}, "paths": {}}
                """.trimIndent()
            )
        }
    }

    private fun apiWithRelativeReference(versionLine: String): Path {

        val directory = Files.createTempDirectory("openapi-downgrading").apply { toFile().deleteOnExit() }
        directory.resolve("schemas").createDirectories().resolve("Thing.yaml").writeText(
            """
            type: object
            properties:
              thingName:
                type: string
            """.trimIndent()
        )
        return directory.resolve("api.yaml").apply {
            writeText(
                """
                $versionLine
                info:
                  title: Things
                  version: 1.0.0
                paths:
                  /things:
                    get:
                      responses:
                        "200":
                          description: Found
                          content:
                            application/json:
                              schema:
                                ${'$'}ref: "./schemas/Thing.yaml"
                """.trimIndent()
            )
        }
    }
}
