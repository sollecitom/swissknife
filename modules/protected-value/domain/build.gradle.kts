dependencies {
    api(projects.swissknifeCoreDomain)
    api(projects.swissknifeCorrelationCoreDomain)

    implementation(projects.swissknifeLoggerCore)

    testImplementation(projects.swissknifeCoreTestUtils)
    testImplementation(projects.swissknifeCorrelationCoreTestUtils)
}