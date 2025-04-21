dependencies {
    api(projects.avroSerializationUtils)
    api(projects.coreDomain)

    implementation(projects.resourceUtils)
    implementation(projects.avroSchemaRepositoryDomain)

    testImplementation(projects.coreTestUtils)
}