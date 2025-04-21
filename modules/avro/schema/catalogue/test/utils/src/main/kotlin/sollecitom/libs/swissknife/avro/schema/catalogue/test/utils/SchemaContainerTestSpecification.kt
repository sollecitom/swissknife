package sollecitom.libs.swissknife.avro.schema.catalogue.test.utils

import assertk.assertThat
import assertk.assertions.doesNotContain
import assertk.assertions.isEqualTo
import assertk.assertions.isSuccess
import assertk.assertions.startsWith
import sollecitom.libs.swissknife.avro.schema.catalogue.domain.AvroSchemaContainer
import org.junit.jupiter.api.Test

interface SchemaContainerTestSpecification {

    val candidate: AvroSchemaContainer

    @Test
    fun `a schema container should be able to return all its referenced schemas`() {

        val result = runCatching { candidate.all.toList() }

        assertThat(result).isSuccess()
    }

    @Test
    fun `a schema container should only refer schemas within its namespace`() {

        candidate.all.forEach { schema ->

            assertThat(schema.namespace).isEqualTo(candidate.namespace.value)
        }
    }

    @Test
    fun `a schema container should only refer nested schema containers with a namespace contained under its own namespace`() {

        candidate.nestedContainers.forEach { nestedContainer ->

            assertThat(nestedContainer.namespace.value).startsWith(candidate.namespace.value)
        }
    }

    @Test
    fun `a schema container should not contain itself`() {

        assertThat(candidate.nestedContainers).doesNotContain(candidate)
    }
}