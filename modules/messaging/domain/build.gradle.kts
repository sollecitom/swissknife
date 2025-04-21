dependencies {
    api(projects.swissknifeDddDomain)
    api(projects.swissknifeReadinessDomain)

    implementation(projects.swissknifeDddLoggingUtils)

    testImplementation(projects.swissknifeDddTestUtils)
    testImplementation(projects.swissknifeMessagingTestUtils)
    testImplementation(projects.swissknifeLoggingStandardSlf4jConfiguration)
}