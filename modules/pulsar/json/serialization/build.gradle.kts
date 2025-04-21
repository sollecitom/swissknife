dependencies {
    api(projects.swissknifeJsonUtils)
    api(projects.swissknifePulsarUtils)

    implementation(projects.swissknifeLoggerCore)

    testImplementation(projects.swissknifeCoreTestUtils)
}