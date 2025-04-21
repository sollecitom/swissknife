dependencies {
    api(projects.webClientInfoDomain)

    implementation(projects.kotlinExtensions)
    implementation(libs.yet.another.user.agent.analyzer)
    implementation(projects.loggerCore)

    runtimeOnly(libs.log4j.to.slf4j)

    testImplementation(projects.correlationCoreTestUtils)
    testImplementation(projects.coreTestUtils)
    testRuntimeOnly(projects.loggerSlf4jAdapter)
}