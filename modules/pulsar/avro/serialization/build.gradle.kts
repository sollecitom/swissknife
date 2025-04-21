dependencies {
    api(projects.avroSerializationUtils)
    api(projects.pulsarUtils)

    implementation(projects.loggerCore)

    testImplementation(projects.coreTestUtils)
}