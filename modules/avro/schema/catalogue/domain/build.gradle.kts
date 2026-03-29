plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(projects.avroSerializationUtils)
    api(projects.coreDomain)

    implementation(projects.resourceUtils)
    implementation(projects.avroSchemaRepositoryDomain)

    testImplementation(projects.coreTestUtils)
}