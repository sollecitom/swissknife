dependencies {
    api(projects.dddDomain)
    api(projects.readinessDomain)

    implementation(projects.dddLoggingUtils)

    testImplementation(projects.dddTestUtils)
    testImplementation(projects.messagingTestUtils)
    testImplementation(projects.loggingStandardSlf4jConfiguration)
}