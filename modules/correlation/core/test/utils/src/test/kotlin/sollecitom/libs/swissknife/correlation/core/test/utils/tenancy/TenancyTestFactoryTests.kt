package sollecitom.libs.swissknife.correlation.core.test.utils.tenancy

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class TenancyTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `with an explicit ID`() {

        val id = newId.external()

        val tenant = Tenant.create(id = id)

        assertThat(tenant.id).isEqualTo(id)
    }
}