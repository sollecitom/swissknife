package sollecitom.libs.swissknife.correlation.core.test.utils.customer

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class CustomerTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `with an explicit ID`() {

        val id = newId.external()

        val customer = Customer.create(id = id)

        assertThat(customer.id).isEqualTo(id)
    }

    @Test
    fun `a test customer`() {

        val isTest = true

        val customer = Customer.create(isTest = isTest)

        assertThat(customer.isTest).isEqualTo(isTest)
    }
}