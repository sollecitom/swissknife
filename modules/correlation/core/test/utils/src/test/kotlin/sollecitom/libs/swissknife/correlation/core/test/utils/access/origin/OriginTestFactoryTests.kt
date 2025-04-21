package sollecitom.libs.swissknife.correlation.core.test.utils.access.origin

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.networking.IpAddress
import sollecitom.libs.swissknife.core.test.utils.testProvider
import sollecitom.libs.swissknife.core.utils.CoreDataGenerator
import sollecitom.libs.swissknife.correlation.core.domain.access.origin.Origin
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class OriginTestFactoryTests : CoreDataGenerator by CoreDataGenerator.testProvider {

    @Test
    fun `the origin generated has the V4 localhost as IP address`() {

        val origin = Origin.create()

        assertThat(origin.ipAddress).isEqualTo(IpAddress.V4.localhost)
    }

    @Test
    fun `the origin has the passed IP V4 address`() {

        val ipAddress = IpAddress.create("152.38.16.4")

        val origin = Origin.create(ipAddress)

        assertThat(origin.ipAddress).isEqualTo(ipAddress)
    }

    @Test
    fun `the origin has the passed IP V6 address`() {

        val ipAddress = IpAddress.create("2001:db8:3333:4444:CCCC:DDDD:EEEE:FFFF")

        val origin = Origin.create(ipAddress)

        assertThat(origin.ipAddress).isEqualTo(ipAddress)
    }
}