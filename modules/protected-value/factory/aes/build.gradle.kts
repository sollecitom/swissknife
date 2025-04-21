dependencies {
    api(projects.swissknifeProtectedValueDomain)
    api(projects.swissknifeCryptographyDomain)

    implementation(projects.swissknifeCryptographyImplementationBouncyCastle)

    testImplementation(projects.swissknifeCorrelationCoreTestUtils)
    testImplementation(projects.swissknifeTestUtils)
}