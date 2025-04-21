dependencies {
    api(platform(libs.http4k.bom))
    api(projects.swissknifeHttp4kUtils)

    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(libs.http4k.server.jetty)
    testImplementation(projects.swissknifeTestUtils)
    testImplementation(libs.http4k.client.apache)
    testImplementation(projects.swissknifeLoggerCore)

    testRuntimeOnly(projects.swissknifeLoggerSlf4jAdapter)
}