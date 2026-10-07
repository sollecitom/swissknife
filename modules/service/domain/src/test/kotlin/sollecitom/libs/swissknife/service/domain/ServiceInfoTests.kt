package sollecitom.libs.swissknife.service.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import sollecitom.libs.swissknife.core.domain.identity.StringId
import sollecitom.libs.swissknife.core.domain.text.Name
import org.http4k.config.Environment
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class ServiceInfoTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class Creation {

        @Test
        fun `creating service info`() {

            val instanceId = StringId("instance-1")
            val name = Name("my-service")

            val info = ServiceInfo(instanceId, name)

            assertThat(info.instanceId).isEqualTo(instanceId)
            assertThat(info.name).isEqualTo(name)
        }

        @Test
        fun `creating service info from the environment uses the instance node name`() {

            val environment = Environment.from("instance.node.name" to "node-1")

            val info = ServiceInfo.fromEnvironment(environment, Name("my-service"))

            assertThat(info).isEqualTo(ServiceInfo(StringId("node-1"), Name("my-service")))
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class WithModuleName {

        @Test
        fun `with module name creates instance info with combined group name`() {

            val instanceId = StringId("instance-1")
            val info = ServiceInfo(instanceId, Name("my-service"))
            val moduleName = Name("http-api")

            val instanceInfo = info.withModuleName(moduleName)

            assertThat(instanceInfo.groupName).isEqualTo(Name("my-service.http-api"))
            assertThat(instanceInfo.nodeName).isEqualTo(Name("instance-1"))
        }
    }
}
