dependencies {
    api(projects.swissknifeWebServiceDomain)
    api(projects.swissknifeWebApiUtils)

    implementation(platform(libs.http4k.bom))
    implementation(libs.http4k.server.jetty)
    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.swissknifeTestUtils)
}
