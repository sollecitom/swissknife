dependencies {
    api(projects.swissknifePulsarJsonSerialization)
    api(projects.swissknifeMessagingDomain)

    implementation(projects.swissknifePulsarMessagingAdapter)
    implementation(projects.swissknifeLoggerCore)

    testImplementation(projects.swissknifeCoreTestUtils)
}