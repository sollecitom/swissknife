package sollecitom.libs.swissknife.avro.schema.repository.domain

import assertk.assertThat
import assertk.assertions.containsExactlyInAnyOrder
import sollecitom.libs.swissknife.core.domain.text.Name
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class LocalResourcesAvroSchemaRepositoryTests {

    @Test
    fun `finding schemas in a namespace after searching another namespace`() {

        val repository = LocalResourcesAvroSchemaRepository(rootPackage = "repository")

        repository.findAllInNamespace(Name("alpha")).toList()
        val schemas = repository.findAllInNamespace(Name("beta")).map { it.resolve().fullName }.toList()

        assertThat(schemas).containsExactlyInAnyOrder("beta.Two")
    }
}
