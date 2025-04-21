dependencies {
    api(projects.swissknifeAvroSerializationUtils)
    api(projects.swissknifePulsarUtils)

    implementation(projects.swissknifeLoggerCore)

    testImplementation(projects.swissknifeCoreTestUtils)
}