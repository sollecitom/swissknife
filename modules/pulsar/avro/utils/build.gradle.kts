dependencies {
    api(projects.pulsarAvroSerialization)
    api(projects.messagingDomain)

    implementation(projects.pulsarMessagingAdapter)
    implementation(projects.loggerCore)

    testImplementation(projects.coreTestUtils)
}