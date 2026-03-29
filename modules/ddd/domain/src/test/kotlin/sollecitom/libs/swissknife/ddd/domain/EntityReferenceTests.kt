package sollecitom.libs.swissknife.ddd.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.correlation.core.domain.access.customer.Customer
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class EntityReferenceTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class ByIdAndCustomer {

        @Test
        fun `creating a reference by id and customer`() {

            val id = StringId("entity-1")
            val customer = Customer(id = StringId("customer-1"), isTest = false)

            val reference = Entity.Reference.ByIdAndCustomer(id, customer)

            assertThat(reference.id).isEqualTo(id)
            assertThat(reference.customer).isEqualTo(customer)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class ByValue {

        @Test
        fun `creating a reference by value delegates id to entity`() {

            val entityId = StringId("entity-2")
            val entity = TestEntity(entityId)

            val reference = Entity.Reference.ByValue(entity)

            assertThat(reference.id).isEqualTo(entityId)
            assertThat(reference.value).isEqualTo(entity)
        }
    }

    private data class TestEntity(override val id: StringId) : Entity
}
