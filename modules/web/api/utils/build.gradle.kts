dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.ops.micrometer)
    api(libs.micrometer.registry.prometheus)
    api(projects.http4kServerUtils)
    api(projects.loggerCore)
    api(projects.configurationUtils)
    api(projects.correlationLoggingUtils)
    api(projects.dddDomain)
    api(projects.serviceReadinessHttp4k)
    api(projects.webApiDomain)

    implementation(projects.correlationLoggingUtils)
    implementation(libs.http4k.server.jetty)
    implementation(projects.kotlinExtensions)
    implementation(projects.lensCorrelationExtensions)
    implementation(projects.webClientInfoAnalyzer)
    implementation(projects.jwtJose4jProcessor)
    implementation(projects.jwtJose4jIssuer)
    implementation(projects.jwtJose4jUtils)

    testImplementation(projects.testUtils)
}
