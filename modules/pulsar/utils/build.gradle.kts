dependencies {
    api(libs.pulsar.client.admin) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
    }
    api(projects.swissknifeCoreDomain)
    api(projects.swissknifeMessagingDomain)
    api(projects.swissknifeReadinessDomain)
    api(projects.swissknifeConfigurationUtils)
    api(projects.swissknifeJsonUtils)

    implementation(projects.swissknifeLoggerCore)

    testImplementation(projects.swissknifeCoreTestUtils)
}