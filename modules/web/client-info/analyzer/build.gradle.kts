dependencies {
    api(projects.swissknifeWebClientInfoDomain)

    implementation(projects.swissknifeKotlinExtensions)
    implementation(libs.yet.another.user.agent.analyzer)
    implementation(projects.swissknifeLoggerCore)

    runtimeOnly(libs.log4j.to.slf4j)

    testImplementation(projects.swissknifeCorrelationCoreTestUtils)
    testImplementation(projects.swissknifeCoreTestUtils)
    testRuntimeOnly(projects.swissknifeLoggerSlf4jAdapter)
}