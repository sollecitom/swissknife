dependencies {
    api(projects.swissknifeOpenapiCheckingChecker)
    api(projects.swissknifeOpenapiBuilder)
    api(projects.swissknifeTestUtils)
    api(projects.swissknifeComplianceCheckerTestUtils)

    implementation(projects.swissknifeKotlinExtensions)

    testImplementation(projects.swissknifeResourceUtils)
}