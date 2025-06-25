dependencies {
    api(libs.pulsar.client.admin) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
    api(projects.coreDomain)
    api(projects.messagingDomain)
    api(projects.readinessDomain)
    api(projects.configurationUtils)
    api(projects.jsonUtils)

    implementation(projects.loggerCore)

    testImplementation(projects.coreTestUtils)
}