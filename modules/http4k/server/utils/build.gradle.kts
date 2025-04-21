dependencies {
    api(platform(libs.http4k.bom))
    api(projects.http4kUtils)

    implementation(projects.kotlinExtensions)

    testImplementation(libs.http4k.server.jetty)
    testImplementation(projects.testUtils)
    testImplementation(libs.http4k.client.apache)
    testImplementation(projects.loggerCore)

    testRuntimeOnly(projects.loggerSlf4jAdapter)
}