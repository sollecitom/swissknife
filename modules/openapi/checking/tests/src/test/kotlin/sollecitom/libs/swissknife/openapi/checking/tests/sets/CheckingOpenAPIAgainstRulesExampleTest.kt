package sollecitom.libs.swissknife.openapi.checking.tests.sets

import assertk.assertThat
import sollecitom.libs.swissknife.compliance.checker.domain.checkAgainstRules
import sollecitom.libs.swissknife.compliance.checker.test.utils.isNotCompliantWithOnlyViolation
import sollecitom.libs.swissknife.openapi.builder.*
import sollecitom.libs.swissknife.openapi.checking.checker.rules.LowercasePathNameRule
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class CheckingOpenAPIAgainstRulesExampleTest {

    @Test
    fun `applying a path rule on an API that's not compliant`() {

        val path = "/allPeople"
        val api = buildOpenApi {
            path(path) {
                get {
                    operationId("get_people")
                    description("Returns a list of people")
                    responses {
                        status(200) {
                            description("Successful response")
                            content {
                                mediaTypes {
                                    add("text/simple")
                                }
                            }
                        }
                    }
                }
            }
        }

        val result = api.checkAgainstRules(LowercasePathNameRule)

        assertThat(result).isNotCompliantWithOnlyViolation(LowercasePathNameRule.Violation(path))
    }
}