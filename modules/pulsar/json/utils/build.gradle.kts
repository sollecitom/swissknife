dependencies {
    api(projects.pulsarJsonSerialization)
    api(projects.messagingDomain)

    implementation(projects.pulsarMessagingAdapter)
    implementation(projects.loggerCore)

    testImplementation(projects.coreTestUtils)
}