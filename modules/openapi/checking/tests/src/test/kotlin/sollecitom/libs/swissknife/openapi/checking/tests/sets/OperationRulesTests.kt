package sollecitom.libs.swissknife.openapi.checking.tests.sets

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod.GET
import io.swagger.v3.oas.models.PathItem.HttpMethod.POST
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isCompliant
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.openapi.builder.*
import sollecitom.libs.swissknife.openapi.checking.checker.model.OpenApiFields
import sollecitom.libs.swissknife.openapi.checking.checker.rule.field.FieldRulesViolation
import sollecitom.libs.swissknife.openapi.checking.checker.rules.EnforceCamelCaseOperationIdRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.EnforceOperationDescriptionDifferentFromSummaryRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.FieldSpecificRules
import sollecitom.libs.swissknife.openapi.checking.checker.rules.ForbiddenRequestBodyRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.LowercasePathNameRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.MandatoryInfoFieldsRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.MandatoryOperationFieldsRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.MandatoryRequestBodyDescriptionRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.MandatoryRequestBodyExampleRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.MandatoryRequestBodyRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.MandatoryResponseBodyExampleRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.WhitelistedAlphabetParameterNameRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.WhitelistedAlphabetPathNameRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.WhitelistedOpenApiVersionFieldRule
import sollecitom.libs.swissknife.openapi.checking.checker.rules.field.MandatorySuffixTextFieldRule
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class OperationRulesTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class LowercasePathName {

        @Test
        fun `template variables are not checked`() {

            val api = buildOpenApi { path("/customers/{customerId}") }

            val result = api.checkAgainstRules(LowercasePathNameRule)

            assertThat(result).isCompliant()
        }

        @Test
        fun `uppercase literal segments are rejected`() {

            val api = buildOpenApi { path("/Customers/{customerId}") }

            val result = api.checkAgainstRules(LowercasePathNameRule)

            assertThat(result).isNotCompliantWithOnlyViolation(LowercasePathNameRule.Violation("/Customers/{customerId}"))
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class EnforceOperationDescriptionDifferentFromSummary {

        @Test
        fun `rejects a description equal to the summary`() {

            val api = buildOpenApi { path("/things") { get { summary("Things"); description("Things") } } }

            val result = api.checkAgainstRules(EnforceOperationDescriptionDifferentFromSummaryRule)

            assertThat(result).isNotCompliantWithOnlyViolation<EnforceOperationDescriptionDifferentFromSummaryRule.Violation, OpenAPI> { violation -> assertThat(violation.operation.pathName).isEqualTo("/things") }
        }

        @Test
        fun `accepts a different description`() {

            val api = buildOpenApi { path("/things") { get { summary("Things"); description("Lists all things") } } }

            val result = api.checkAgainstRules(EnforceOperationDescriptionDifferentFromSummaryRule)

            assertThat(result).isCompliant()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class EnforceCamelCaseOperationId {

        @Test
        fun `rejects a snake case operation id`() {

            val api = buildOpenApi { path("/things") { get { operationId("get_things") } } }

            val result = api.checkAgainstRules(EnforceCamelCaseOperationIdRule)

            assertThat(result).isNotCompliantWithOnlyViolation<EnforceCamelCaseOperationIdRule.Violation, OpenAPI> { violation -> assertThat(violation.operation.operationId).isEqualTo("get_things") }
        }

        @Test
        fun `accepts a camel case operation id`() {

            val api = buildOpenApi { path("/things") { get { operationId("getThings") } } }

            val result = api.checkAgainstRules(EnforceCamelCaseOperationIdRule)

            assertThat(result).isCompliant()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class WhitelistedAlphabetParameterName {

        private val rule = WhitelistedAlphabetParameterNameRule(pathAlphabet = ('a'..'z').toSet() + '-')

        @Test
        fun `rejects a parameter name outside the alphabet`() {

            val api = buildOpenApi { path("/things") { get { parameters { add { name("page_size").`in`("query") } } } } }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation, OpenAPI> { violation -> assertThat(violation.parameter.name).isEqualTo("page_size") }
        }

        @Test
        fun `accepts a parameter name within the alphabet`() {

            val api = buildOpenApi { path("/things") { get { parameters { add { name("page-size").`in`("query") } } } } }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isCompliant()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class WhitelistedAlphabetPathName {

        private val rule = WhitelistedAlphabetPathNameRule(alphabet = ('a'..'z').toSet() + '-')

        @Test
        fun `rejects a path segment outside the alphabet`() {

            val api = buildOpenApi { path("/all_things") }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isNotCompliantWithOnlyViolation(WhitelistedAlphabetPathNameRule.Violation("/all_things", rule.alphabet))
        }

        @Test
        fun `accepts path segments within the alphabet`() {

            val api = buildOpenApi { path("/all-things/{id}") }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isCompliant()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class MandatoryInfoFields {

        private val rule = MandatoryInfoFieldsRule(setOf(OpenApiFields.Info.title, OpenApiFields.Info.version))

        @Test
        fun `rejects missing info fields`() {

            val api = buildOpenApi { info { title("Things API") } }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryInfoFieldsRule.Violation, OpenAPI> { violation -> assertThat(violation.missingRequiredFields).containsOnly(OpenApiFields.Info.version) }
        }

        @Test
        fun `accepts all info fields`() {

            val api = buildOpenApi { info { title("Things API"); version("1.0.0") } }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isCompliant()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class MandatoryOperationFields {

        private val rule = MandatoryOperationFieldsRule(setOf(OpenApiFields.Operation.summary))

        @Test
        fun `rejects an operation without the mandatory fields`() {

            val api = buildOpenApi { path("/things") { get { description("Lists all things") } } }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryOperationFieldsRule.Violation, OpenAPI> { violation -> assertThat(violation.missingRequiredFields).containsOnly(OpenApiFields.Operation.summary) }
        }

        @Test
        fun `accepts an operation with the mandatory fields`() {

            val api = buildOpenApi { path("/things") { get { summary("Things") } } }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isCompliant()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class WhitelistedOpenApiVersionField {

        @Test
        fun `rejects a version that is not whitelisted`() {

            val api = buildOpenApi { version("3.0.0") }

            val result = api.checkAgainstRules(WhitelistedOpenApiVersionFieldRule(setOf("3.1.0")))

            assertThat(result).isNotCompliantWithOnlyViolation(WhitelistedOpenApiVersionFieldRule.Violation("3.0.0", setOf("3.1.0")))
        }

        @Test
        fun `accepts a whitelisted version`() {

            val api = buildOpenApi { version("3.1.0") }

            val result = api.checkAgainstRules(WhitelistedOpenApiVersionFieldRule(setOf("3.1.0")))

            assertThat(result).isCompliant()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class RequestBodyRules {

        @Test
        fun `a mandatory request body must be present`() {

            val api = buildOpenApi { path("/things") { post { } } }

            val result = api.checkAgainstRules(MandatoryRequestBodyRule(setOf(POST to true)))

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyRule.Violation, OpenAPI> { violation -> assertThat(violation.requiredBody).isEqualTo(true) }
        }

        @Test
        fun `a mandatory request body that is required is accepted`() {

            val api = buildOpenApi { path("/things") { post { requestBody { required(true) } } } }

            val result = api.checkAgainstRules(MandatoryRequestBodyRule(setOf(POST to true)))

            assertThat(result).isCompliant()
        }

        @Test
        fun `a forbidden request body is rejected`() {

            val api = buildOpenApi { path("/things") { get { requestBody { description("Not allowed") } } } }

            val result = api.checkAgainstRules(ForbiddenRequestBodyRule(setOf(GET)))

            assertThat(result).isNotCompliantWithOnlyViolation<ForbiddenRequestBodyRule.Violation, OpenAPI> { violation -> assertThat(violation.operation.pathName).isEqualTo("/things") }
        }

        @Test
        fun `a request body without a description is rejected`() {

            val api = buildOpenApi { path("/things") { post { requestBody { required(true) } } } }

            val result = api.checkAgainstRules(MandatoryRequestBodyDescriptionRule(setOf(POST)))

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyDescriptionRule.Violation, OpenAPI> { violation -> assertThat(violation.operation.pathName).isEqualTo("/things") }
        }

        @Test
        fun `a request body without an example is rejected`() {

            val api = buildOpenApi { path("/things") { post { requestBody { content { mediaTypes { add("application/json") } } } } } }

            val result = api.checkAgainstRules(MandatoryRequestBodyExampleRule(setOf(POST), setOf("application/json")))

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryRequestBodyExampleRule.Violation, OpenAPI> { violation -> assertThat(violation.mediaTypesWithoutMandatoryExample).containsOnly("application/json") }
        }

        @Test
        fun `a request body with an example is accepted`() {

            val api = buildOpenApi { path("/things") { post { requestBody { content { mediaTypes { add("application/json") { example = mapOf("name" to "thing") } } } } } } }

            val result = api.checkAgainstRules(MandatoryRequestBodyExampleRule(setOf(POST), setOf("application/json")))

            assertThat(result).isCompliant()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class MandatoryResponseBodyExample {

        private val rule = MandatoryResponseBodyExampleRule(setOf(GET), setOf("application/json"))

        @Test
        fun `a response body without an example is rejected`() {

            val api = buildOpenApi { path("/things") { get { responses { status(200) { description("Found"); content { mediaTypes { add("application/json") } } } } } } }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryResponseBodyExampleRule.Violation, OpenAPI> { violation -> assertThat(violation.responsesWithoutAMandatoryExample).isEqualTo(mapOf("200" to setOf("application/json"))) }
        }

        @Test
        fun `a response body with an example is accepted`() {

            val api = buildOpenApi { path("/things") { get { responses { status(200) { description("Found"); content { mediaTypes { add("application/json") { example = listOf("thing") } } } } } } } }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isCompliant()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class FieldSpecific {

        private val rule = FieldSpecificRules(OpenApiFields.Operation.summary to setOf(MandatorySuffixTextFieldRule(suffix = ".", ignoreCase = false)))

        @Test
        fun `a field breaking its rule is rejected`() {

            val api = buildOpenApi { path("/things") { get { summary("Lists things") } } }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isNotCompliantWithOnlyViolation<FieldRulesViolation<String>, OpenAPI> { violation -> assertThat(violation.fieldViolations).containsOnly(MandatorySuffixTextFieldRule.Violation("Lists things", ".", false)) }
        }

        @Test
        fun `a field following its rule is accepted`() {

            val api = buildOpenApi { path("/things") { get { summary("Lists things.") } } }

            val result = api.checkAgainstRules(rule)

            assertThat(result).isCompliant()
        }
    }
}
