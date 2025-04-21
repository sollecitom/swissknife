dependencies {
    api(projects.swissknifeAvroSerializationUtils)
    api(projects.swissknifeCoreDomain)

    implementation(projects.swissknifeResourceUtils)
    implementation(projects.swissknifeAvroSchemaRepositoryDomain)

    testImplementation(projects.swissknifeCoreTestUtils)
}