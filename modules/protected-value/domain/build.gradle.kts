dependencies {
    api(projects.coreDomain)
    api(projects.correlationCoreDomain)

    implementation(projects.loggerCore)

    testImplementation(projects.coreTestUtils)
    testImplementation(projects.correlationCoreTestUtils)
}