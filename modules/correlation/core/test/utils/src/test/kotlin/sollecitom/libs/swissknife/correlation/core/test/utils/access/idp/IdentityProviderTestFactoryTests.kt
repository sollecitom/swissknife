package sollecitom.libs.swissknife.correlation.core.test.utils.access.idp

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.text.Name
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.idp.IdentityProvider
import sollecitom.libs.swissknife.correlation.core.domain.tenancy.Tenant
import sollecitom.libs.swissknife.correlation.core.test.utils.tenancy.create
import sollecitom.libs.swissknife.kotlin.extensions.text.CharacterGroups.letters
import sollecitom.libs.swissknife.kotlin.extensions.text.string
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class IdentityProviderTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `with given name and tenant`() {

        val name = random.string(wordLength = 10, alphabet = letters).let(::Name)
        val tenant = Tenant.create()

        val identityProvider = IdentityProvider.create(name = name, tenant = tenant)

        assertThat(identityProvider.name).isEqualTo(name)
        assertThat(identityProvider.tenant).isEqualTo(tenant)
    }
}