package sollecitom.libs.swissknife.openapi.builder

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.SpecVersion
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
class OpenApiBuilderTests {

    @Nested
    @TestInstance(PER_CLASS)
    inner class BuildingAnApi {

        @Test
        fun `building an empty API`() {

            val api = buildOpenApi { }

            assertThat(api).isNotNull()
        }

        @Test
        fun `building an API with a version`() {

            val api = buildOpenApi {
                version(OpenApiBuilder.OpenApiVersion.V3_1_0)
            }

            assertThat(api.openapi).isEqualTo("3.1.0")
        }

        @Test
        fun `building an API with a string version`() {

            val api = buildOpenApi {
                version("3.0.1")
            }

            assertThat(api.openapi).isEqualTo("3.0.1")
        }

        @Test
        fun `building an API with info`() {

            val api = buildOpenApi {
                info {
                    title = "Test API"
                    description = "A test API"
                }
            }

            assertThat(api.info.title).isEqualTo("Test API")
            assertThat(api.info.description).isEqualTo("A test API")
        }

        @Test
        fun `building an API with a path`() {

            val api = buildOpenApi {
                path("/users") { }
            }

            assertThat(api.paths).isNotNull()
            assertThat(api.paths.containsKey("/users")).isEqualTo(true)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class PathItemExtensions {

        @Test
        fun `adding a GET operation to a path`() {

            val api = buildOpenApi {
                path("/users") {
                    get {
                        operationId = "getUsers"
                    }
                }
            }

            assertThat(api.paths["/users"]?.get?.operationId).isEqualTo("getUsers")
        }

        @Test
        fun `adding a POST operation to a path`() {

            val api = buildOpenApi {
                path("/users") {
                    post {
                        operationId = "createUser"
                    }
                }
            }

            assertThat(api.paths["/users"]?.post?.operationId).isEqualTo("createUser")
        }

        @Test
        fun `adding a PUT operation to a path`() {

            val api = buildOpenApi {
                path("/users") {
                    put {
                        operationId = "updateUser"
                    }
                }
            }

            assertThat(api.paths["/users"]?.put?.operationId).isEqualTo("updateUser")
        }

        @Test
        fun `adding a DELETE operation to a path`() {

            val api = buildOpenApi {
                path("/users") {
                    delete {
                        operationId = "deleteUser"
                    }
                }
            }

            assertThat(api.paths["/users"]?.delete?.operationId).isEqualTo("deleteUser")
        }

        @Test
        fun `adding a PATCH operation to a path`() {

            val api = buildOpenApi {
                path("/users") {
                    patch {
                        operationId = "patchUser"
                    }
                }
            }

            assertThat(api.paths["/users"]?.patch?.operationId).isEqualTo("patchUser")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class OperationExtensions {

        @Test
        fun `adding parameters to an operation`() {

            val api = buildOpenApi {
                path("/users/{id}") {
                    get {
                        parameters {
                            add {
                                name = "id"
                                `in` = "path"
                                required = true
                            }
                        }
                    }
                }
            }

            val parameters = api.paths["/users/{id}"]?.get?.parameters
            assertThat(parameters).isNotNull()
            assertThat(parameters!!.size).isEqualTo(1)
            assertThat(parameters[0].name).isEqualTo("id")
        }

        @Test
        fun `adding responses to an operation`() {

            val api = buildOpenApi {
                path("/users") {
                    get {
                        responses {
                            status(200) {
                                description = "Success"
                            }
                            status(404) {
                                description = "Not Found"
                            }
                        }
                    }
                }
            }

            val responses = api.paths["/users"]?.get?.responses
            assertThat(responses).isNotNull()
            assertThat(responses!!["200"]?.description).isEqualTo("Success")
            assertThat(responses["404"]?.description).isEqualTo("Not Found")
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class OpenApiVersionEnum {

        @Test
        fun `V3_1_0 has the correct value`() {

            assertThat(OpenApiBuilder.OpenApiVersion.V3_1_0.value).isEqualTo("3.1.0")
        }

        @Test
        fun `V3_0_1 has the correct value`() {

            assertThat(OpenApiBuilder.OpenApiVersion.V3_0_1.value).isEqualTo("3.0.1")
        }
    }
}
