dependencies {
    api(projects.protectedValueDomain)
    api(projects.cryptographyDomain)

    implementation(projects.cryptographyImplementationBouncyCastle)

    testImplementation(projects.correlationCoreTestUtils)
    testImplementation(projects.testUtils)
}