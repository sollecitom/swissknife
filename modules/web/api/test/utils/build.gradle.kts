dependencies {
    api(projects.swissknifeWebServiceDomain)
    api(projects.swissknifeWebApiUtils)
    api(projects.swissknifeTestUtils)
    api(platform(libs.http4k.bom))
    api(libs.http4k.client.apache.async)
    api(projects.swissknifeOpenapiValidationHttp4kTestUtils)
    api(projects.swissknifeCoreTestUtils)

    implementation(projects.swissknifeKotlinExtensions)
}
