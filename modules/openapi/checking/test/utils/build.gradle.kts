dependencies {
    api(projects.openapiCheckingChecker)
    api(projects.openapiBuilder)
    api(projects.testUtils)
    api(projects.complianceCheckerTestUtils)

    implementation(projects.kotlinExtensions)

    testImplementation(projects.resourceUtils)
}