dependencies {
    api(projects.swissknifeDddDomain)
    api(projects.swissknifeLoggerCore)
    api(projects.swissknifeKotlinExtensions)
    api(projects.swissknifeCorrelationLoggingUtils)

    testImplementation(projects.swissknifeTestUtils)
}