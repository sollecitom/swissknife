package sollecitom.libs.swissknife.openapi.checking.tests.sets

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.swagger.v3.core.util.Json
import io.swagger.v3.oas.models.PathItem.HttpMethod.POST
import io.swagger.v3.oas.models.examples.Example
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.parameters.Parameter
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isCompliant
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.openapi.builder.*
import sollecitom.libs.swissknife.openapi.checking.checker.rules.DisallowReservedCharactersInParameterNameRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.ExamplesSchemaComplianceRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.MandatoryRequestBodyContentMediaTypesRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.MandatoryVersioningPathPrefixRule
import io.swagger.v3.oas.models.OpenAPI
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class OpenApiRulesTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class DisallowReservedCharactersInParameterName {

        @Test
        fun `applies to parameters declared for the whole path`() {

            val api = buildOpenApi {
                path("/things") {
                    parameters = listOf(Parameter().name("filter").`in`("query").allowReserved(true))
                    get {
                        responses {
                            status(200) {
                                description("Found")
                            }
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(DisallowReservedCharactersInParameterNameRule)

            assertThat(result).isNotCompliantWithOnlyViolation<DisallowReservedCharactersInParameterNameRule.Violation, OpenAPI> { violation ->
                assertThat(violation.parameter.name).isEqualTo("filter")
            }
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class MandatoryVersioningPathPrefix {

        private val rule = MandatoryVersioningPathPrefixRule()

        @Test
        fun `accepts a version with multiple digits`() {

            val api = buildOpenApi { path("/v10/things") }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isCompliant()
        }

        @Test
        fun `rejects a version that is not a prefix of the path`() {

            val api = buildOpenApi { path("/things/v1/details") }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isNotCompliantWithOnlyViolation(MandatoryVersioningPathPrefixRule.Violation("/things/v1/details", 1))
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class MandatoryRequestBodyContentMediaTypes {

        @Test
        fun `rejects a request body without content`() {

            val api = buildOpenApi {
                path("/things") {
                    post {
                        requestBody {
                            description = "The thing to add."
                        }
                    }
                }
            }

            val result = api.checkAgainstRules(MandatoryRequestBodyContentMediaTypesRule(methodsToCheck = setOf(POST)))

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyContentMediaTypesRule.Violation, OpenAPI> { violation ->
                assertThat(violation.operation.pathName).isEqualTo("/things")
            }
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class ExamplesSchemaCompliance {

        private val rule = ExamplesSchemaComplianceRule(mediaTypesToCheck = setOf("application/json"))

        @Test
        fun `accepts an array example that complies with its schema`() {

            val api = apiWithResponseMediaType {
                schema = Schema<Any>().`$ref`("#/components/schemas/Things")
                examples = mapOf("Things" to Example().value(Json.mapper().readTree("""[{"id":1}]""")))
            }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isCompliant()
        }

        @Test
        fun `ignores examples with an external value`() {

            val api = apiWithResponseMediaType {
                schema = Schema<Any>().`$ref`("#/components/schemas/Things")
                examples = mapOf("Things" to Example().externalValue("https://example.com/things.json"))
            }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isCompliant()
        }

        private fun apiWithResponseMediaType(customize: io.swagger.v3.oas.models.media.MediaType.() -> Unit) = buildOpenApi {
            path("/things") {
                get {
                    responses {
                        status(200) {
                            description("Found")
                            content {
                                mediaTypes {
                                    add("application/json", customize)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}