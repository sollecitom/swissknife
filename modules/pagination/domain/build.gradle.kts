dependencies {
    api(projects.swissknifeCoreDomain)
    api(projects.swissknifeCorrelationCoreDomain)
    api(projects.swissknifeKotlinExtensions)
    api(projects.swissknifeCoreUtils)

    testImplementation(projects.swissknifeTestUtils)
}