dependencies {
    api(projects.swissknifePulsarAvroSerialization)
    api(projects.swissknifeMessagingDomain)

    implementation(projects.swissknifePulsarMessagingAdapter)
    implementation(projects.swissknifeLoggerCore)

    testImplementation(projects.swissknifeCoreTestUtils)
}