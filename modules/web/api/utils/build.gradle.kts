dependencies {
    api(platform(libs.http4k.bom))
    api(libs.http4k.ops.micrometer)
    api(libs.micrometer.registry.prometheus)
    api(projects.swissknifeHttp4kServerUtils)
    api(projects.swissknifeLoggerCore)
    api(projects.swissknifeConfigurationUtils)
    api(projects.swissknifeCorrelationLoggingUtils)
    api(projects.swissknifeDddDomain)
    api(projects.swissknifeServiceReadinessHttp4k)
    api(projects.swissknifeWebApiDomain)

    implementation(projects.swissknifeCorrelationLoggingUtils)
    implementation(libs.http4k.server.jetty)
    implementation(projects.swissknifeKotlinExtensions)
    implementation(projects.swissknifeLensCorrelationExtensions)
    implementation(projects.swissknifeWebClientInfoAnalyzer)
    implementation(projects.swissknifeJwtJose4jProcessor)
    implementation(projects.swissknifeJwtJose4jIssuer)
    implementation(projects.swissknifeJwtJose4jUtils)

    testImplementation(projects.swissknifeTestUtils)
}
