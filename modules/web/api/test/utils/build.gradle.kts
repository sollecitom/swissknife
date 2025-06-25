dependencies {
    api(projects.webServiceDomain)
    api(projects.webApiUtils)
    api(projects.testUtils)
    api(platform(libs.http4k.bom))
    api(libs.http4k.client.apache.async)
    api(projects.openapiValidationHttp4kTestUtils)
    api(projects.coreTestUtils)

    implementation(projects.kotlinExtensions)
}
