dependencies {
    api(projects.webServiceDomain)
    api(projects.webApiUtils)

    implementation(platform(libs.http4k.bom))
    implementation(libs.http4k.server.jetty)
    implementation(projects.kotlinExtensions)

    testImplementation(projects.testUtils)
}
