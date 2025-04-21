dependencies {
    api(projects.swissknifeCoreDomain)
    api(projects.swissknifeCorrelationCoreDomain)
    api(projects.swissknifeKotlinExtensions)
    api(projects.swissknifeCoreUtils)
    api(projects.swissknifeLoggerCore)

    testImplementation(projects.swissknifeTestUtils)
}